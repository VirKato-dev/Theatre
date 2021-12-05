package my.example.theatre;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


/***
 * Экран администратора для регистрации кассиров, фильмов и сеансов показа
 */
public class AdminActivity extends AppCompatActivity {

    // сначала определим названия и типы переменных для хранения ссылок на виджеты экрана
    // форма редактирования сеанса
    private LinearLayout l_session_edit;
    private Spinner spin_hall_session;
    private LinearLayout l_date_session;
    private TextView t_date_session;
    private LinearLayout l_time_session;
    private TextView t_time_session;
    private Spinner spin_cinema_session;
    private EditText e_price_session;
    private Button b_save_session;
    private RecyclerView rv_sessions;
    private SessionsAdapter sessionsAdapter;

    // форма редактирования кассира
    private LinearLayout l_cashier_edit;
    private EditText e_cashier_name;
    private EditText e_cashier_login;
    private EditText e_cashier_password;
    private Button b_save_cashier;
    private RecyclerView rv_cashiers;
    private CashiersAdapter cashiersAdapter;

    // форма редактирования фильма
    private LinearLayout l_cinema_edit;
    private EditText e_cinema_name;
    private LinearLayout l_duration_cinema;
    private TextView t_duration_cinema;
    private EditText e_cinema_description;
    private Button b_save_cinema;
    private RecyclerView rv_cinemas;
    private CinemasAdapter cinemasAdapter;

    // кнопка добавления
    private FloatingActionButton fab_add_data;

    // нижние кнопки переключения режимов
    private LinearLayout l_bottom_buttons;
    private Button b_cashier;
    private Button b_cinema;
    private Button b_session;

    // переменные алгоритма
    private Cashier cashier = new Cashier();
    private Cinema cinema = new Cinema();
    private Session session = new Session();
    private ArrayList<Cashier> cashiers = new ArrayList<>();
    private ArrayList<Cinema> cinemas = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();
    private int screenNumber = 0;


    /***
     * Первичная настройка всех элементов и данных
     * @param savedInstanceState сохранённое состояние Активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTitle("Администратор");

        // сохраним ссылки на элементы экрана в соответствующие переменные

        // форма редактирования кассира
        l_cashier_edit = findViewById(R.id.l_cashier_edit);
        e_cashier_name = findViewById(R.id.e_cashier_name);
        e_cashier_login = findViewById(R.id.e_cashier_login);
        e_cashier_password = findViewById(R.id.e_cashier_password);
        b_save_cashier = findViewById(R.id.b_save_cashier);
        b_save_cashier.setOnClickListener(v -> {
            showScreen(0);
            saveCashierData();
        });
        rv_cashiers = findViewById(R.id.rv_cashiers);
        // получить список кассиров
        cashiers = FileDatabase.getCashiers();
        // задать способ взаимного расположения элементов списка на экране (Вертикальный скролл)
        rv_cashiers.setLayoutManager(new LinearLayoutManager(this));
        // привязать список кассиров
        cashiersAdapter = new CashiersAdapter(cashiers);
        cashiersAdapter.setClickListener((view, position) -> editCashierData(cashiersAdapter.getItem(position)));
        // задать обработчик долгого нажатия на элемент списка
        cashiersAdapter.setLongClickListener((view, position) -> removeCashierData(cashiersAdapter.getItem(position)));
        // связываем виджет списка с адаптером данных списка
        rv_cashiers.setAdapter(cashiersAdapter);

        // форма редактирования фильма
        l_cinema_edit = findViewById(R.id.l_cinema_edit);
        e_cinema_name = findViewById(R.id.e_cinema_name);
        e_cinema_description = findViewById(R.id.e_cinema_description);
        t_duration_cinema = findViewById(R.id.t_duration_cinema);
        l_duration_cinema = findViewById(R.id.l_duration_cinema);
        bindTimePickerTo(l_duration_cinema, t_duration_cinema, "HH:mm");
        b_save_cinema = findViewById(R.id.b_save_cinema);
        b_save_cinema.setOnClickListener(v -> {
            showScreen(1);
            saveCinemaData();
        });
        rv_cinemas = findViewById(R.id.rv_cinemas);
        // получить список фильмов
        cinemas = FileDatabase.getCinemas();
        // задать способ взаимного расположения элементов списка на экране (Вертикальный скролл)
        rv_cinemas.setLayoutManager(new LinearLayoutManager(this));
        // привязать список фильмов
        cinemasAdapter = new CinemasAdapter(cinemas);
        cinemasAdapter.setClickListener((view, position) -> editCinemaData(cinemasAdapter.getItem(position)));
        // задать обработчик долгого нажатия на элемент списка
        cinemasAdapter.setLongClickListener((view, position) -> removeCinemaData(cinemasAdapter.getItem(position)));
        // связываем виджет списка с адаптером данных списка
        rv_cinemas.setAdapter(cinemasAdapter);

        // форма редактирования сеанса
        l_session_edit = findViewById(R.id.l_session_edit);
        spin_hall_session = findViewById(R.id.spin_hall_session);
        spin_hall_session.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Hall.names));
        t_date_session = findViewById(R.id.t_date_session);
        l_date_session = findViewById(R.id.l_date_session);
        bindDatePickerTo(l_date_session, t_date_session, "dd/MM/yyyy");
        t_time_session = findViewById(R.id.t_time_session);
        l_time_session = findViewById(R.id.l_time_session);
        bindTimePickerTo(l_time_session, t_time_session, "HH:mm");
        spin_cinema_session = findViewById(R.id.spin_cinema_session);
        e_price_session = findViewById(R.id.e_price_session);
        b_save_session = findViewById(R.id.b_save_session);
        b_save_session.setOnClickListener(v -> {
            // new View.OnClickListener превратился в лямбда-функцию.
            // Здесь и далее в коде часто будет использоваться лямбда-функция для упрощения
            // v - передаваемый в функцию параметр (здесь это кликнутая кнопка)
            showScreen(2);
            saveSessionData();
        });
        rv_sessions = findViewById(R.id.rv_sessions);
        // получить список сеансов
        sessions = FileDatabase.getSessions();
        // задать способ взаимного расположения элементов списка на экране (Вертикальный скролл)
        rv_sessions.setLayoutManager(new LinearLayoutManager(this));
        // привязать список сеансов
        sessionsAdapter = new SessionsAdapter(sessions);
        sessionsAdapter.setClickListener((view, position) -> editSessionData(sessionsAdapter.getItem(position)));
        // задать обработчик долгого нажатия на элемент списка
        sessionsAdapter.setLongClickListener((view, position) -> removeSessionData(sessionsAdapter.getItem(position)));
        // связываем виджет списка с адаптером данных списка
        rv_sessions.setAdapter(sessionsAdapter);

        // кнопка добавления
        fab_add_data = findViewById(R.id.fab_add_data);
        fab_add_data.setOnClickListener(v -> {
            // тип создаваемых данных зависит от выбранного экрана
            switch (screenNumber) {
                case 0:
                    addNewCashier();
                    break;
                case 1:
                    addNewCinema();
                    break;
                case 2:
                    addNewSession();
            }
            showEdit(true);
        });

        // нижние кнопки переключения экранов для выбора типа редактируемых данных
        l_bottom_buttons = findViewById(R.id.l_bottom_buttons);
        b_cashier = findViewById(R.id.b_cashier);
        b_cashier.setOnClickListener(v -> {
            showScreen(0);
        });
        b_cinema = findViewById(R.id.b_cinema);
        b_cinema.setOnClickListener(v -> {
            showScreen(1);
        });
        b_session = findViewById(R.id.b_session);
        b_session.setOnClickListener(v -> {
            showScreen(2);
        });

        // первичный режим устанавливаем для работы с данными о кассирах
        showScreen(0);
    }

    /***
     * меню справа сверху
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Выйти из аккаунта");
        return super.onCreateOptionsMenu(menu);
    }

    /***
     * сработает при выборе любого пункта меню (справа сверху)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        sp.edit().remove("login").apply();
        sp.edit().remove("password").apply();
        finish();
        return super.onOptionsItemSelected(item);
    }

    /***
     * Показать макет экрана соответствующий текущему режиму редактирования без демонстрации формы для редактирования данных:
     * 0 - кассир; 1 - фильм; 2 - сеанс.
     */
    private void showScreen(int page) {
        screenNumber = page;
        refreshLists();
        showEdit(false);
        // откроем только список выбранного режима
        rv_cashiers.setVisibility(0 == page ? View.VISIBLE : View.GONE);
        rv_cinemas.setVisibility(1 == page ? View.VISIBLE : View.GONE);
        rv_sessions.setVisibility(2 == page ? View.VISIBLE : View.GONE);
        // спрячем кнопку перехода на текущий режим
        b_cashier.setVisibility(0 == page ? View.GONE : View.VISIBLE);
        b_cinema.setVisibility(1 == page ? View.GONE : View.VISIBLE);
        b_session.setVisibility(2 == page ? View.GONE : View.VISIBLE);
        // изменим заголовок экрана
        String[] titles = new String[]{"Кассиры", "Фильмы", "Сеансы"};
        setTitle("Администратор - " + titles[screenNumber]);

    }

    /***
     * Получить свежие списки из базы данных (из файла) и обновить их на экране
     */
    private void refreshLists() {
        switch (screenNumber) {
            case 0:
                cashiers = FileDatabase.getCashiers();
                cashiersAdapter.setNewList(cashiers);
                break;
            case 1:
                cinemas = FileDatabase.getCinemas();
                cinemasAdapter.setNewList(cinemas);
                break;
            case 2:
                sessions = FileDatabase.getSessions();
                sessionsAdapter.setNewList(sessions);
        }
    }

    /***
     * Показать/спрятать форму редактирования данных для текущего режима
     * @param show показать?
     */
    private void showEdit(boolean show) {
        // используем тернарное выражение вместо if-else для экономии места
        // (условие) ? (если истина) : (если ложь)
        l_cashier_edit.setVisibility(show && 0 == screenNumber ? View.VISIBLE : View.GONE);
        l_cinema_edit.setVisibility(show && 1 == screenNumber ? View.VISIBLE : View.GONE);
        l_session_edit.setVisibility(show && 2 == screenNumber ? View.VISIBLE : View.GONE);
        // спрячем плавающую кнопку добавления новых данных (справа снизу), если форма показана
        fab_add_data.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /***
     * Показать форму для создания нового кассира
     */
    private void addNewCashier() {
        // не режим редактирования
        cashier = new Cashier();
        // Очистить поля ввода для создания нового кассира
        e_cashier_name.setText("");
        e_cashier_login.setText("");
        e_cashier_password.setText("");
        showEdit(true);
    }

    /***
     * Показать данные кассира для редактирования
     * @param cashier редактируемый кассир
     */
    private void editCashierData(Cashier cashier) {
        // режим редактирования
        this.cashier = cashier;
        // заполняем форму соответствующими данными
        e_cashier_login.setText(cashier.login);
        e_cashier_password.setText(cashier.password);
        e_cashier_name.setText(cashier.name);
        showEdit(true);
    }

    /***
     * Удалить данные о кассире
     * @param cashier удаляемый кассир
     */
    private void removeCashierData(Cashier cashier) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление кассира");
        builder.setMessage("Вы действительно желаете удалить этого кассира '" + cashier.name + "' из базы?");
        builder.setPositiveButton("Удалить", (dialog, which) -> {
            // Удалить кассира из базы
            FileDatabase.removeUserById(cashier.login);
            // Получить обновлённый список кассиров
            ArrayList<Cashier> cashiers = FileDatabase.getCashiers();
            // Показать обновлённый список кассиров
            cashiersAdapter.setNewList(cashiers);
            showEdit(false);
        });
        builder.setNegativeButton("Отменить", (dialog, which) -> {
            // Просто закрыть диалог, как и при тапе за пределами окна диалога
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /***
     * Сохранить данные кассира
     */
    private void saveCashierData() {
        String login = e_cashier_login.getText().toString().trim();
        String password = e_cashier_password.getText().toString().trim();
        String name = e_cashier_name.getText().toString().trim();
        if (login.equals("") || password.equals("") || name.equals("")) {
            Toast.makeText(this, "Все поля обязательны для заполнения", Toast.LENGTH_LONG).show();
        } else {
            FileDatabase.removeUserById(login);
            // Создаём человека и указываем его данные
            cashier = new Cashier();
            cashier.login = login;
            cashier.password = password;
            cashier.name = name;
            // Проверяем уникальность логина
            Cashier h = FileDatabase.findCashierById(cashier.login);
            if (h.login.equals("")) {
                // Когда не найден кассир с таким логином - можно добавить кассира с этим логином
                FileDatabase.addUser(cashier);
                // Получить обновлённый список кассиров
                cashiers = FileDatabase.getCashiers();
                // Показать обновлённый список кассиров
                cashiersAdapter.setNewList(cashiers);
                // Спрятать форму редактирования данных
                showEdit(false);
            }
        }
    }

    /***
     * Показать форму для создания нового фильма
     */
    private void addNewCinema() {
        // не режим редактирования
        cinema = new Cinema();
        // Очистить поля ввода для создания нового фильма
        e_cinema_name.setText("");
        t_duration_cinema.setText("");
        e_cinema_description.setText("");
        showEdit(true);
    }

    /***
     * Показать данные фильма для редактирования
     * @param cinema редактируемый фильм
     */
    private void editCinemaData(Cinema cinema) {
        // режим редактирования
        this.cinema = cinema;
        // заполняем форму соответствующими данными
        e_cinema_name.setText(cinema.name);
        t_duration_cinema.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(cinema.duration));
        e_cinema_description.setText(cinema.description);
        showEdit(true);
    }

    /***
     * Удалить данные о фильме
     * @param cinema удаляемый фильм
     */
    private void removeCinemaData(Cinema cinema) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление фильма");
        builder.setMessage("Вы действительно желаете удалить этот фильм '" + cinema.name + "' из базы?");
        builder.setPositiveButton("Удалить", (dialog, which) -> {
            // Удалить фильм из базы
            FileDatabase.removeCinemaById(cinema.id);
            // Получить обновлённый список фильмов
            ArrayList<Cinema> cinemas = FileDatabase.getCinemas();
            // Показать обновлённый список фильмов
            cinemasAdapter.setNewList(cinemas);
            showEdit(false);
        });
        builder.setNegativeButton("Отменить", (dialog, which) -> {
            // Просто закрыть диалог, как и при тапе за пределами окна диалога
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /***
     * Сохранить данные о фильме
     */
    private void saveCinemaData() {
        String id = "";
        String name = e_cinema_name.getText().toString().trim();
        String dur = t_duration_cinema.getText().toString().trim();
        String desc = e_cinema_description.getText().toString().trim();
        if (name.equals("") || desc.equals("") || dur.equals("")) {
            Toast.makeText(this, "Все поля обязательны для заполнения", Toast.LENGTH_LONG).show();
        } else {
            if (cinema.id.equals("")) {
                // id пустой - создаётся новый фильм
                id = genCinemaID();
            } else {
                // id не пустой - редактируется существующий фильм
                id = cinema.id;
                FileDatabase.removeCinemaById(cinema.id);
            }
            // Создаём фильм и указываем его данные
            cinema = new Cinema();
            cinema.id = id;
            cinema.name = name;
            cinema.duration = new SimpleDateFormat("HH:mm", Locale.getDefault())
                    .parse(dur, new ParsePosition(0)).getTime();
            cinema.description = desc;
            // Проверяем уникальность ID
            Cinema c = FileDatabase.findCinemaById(cinema.id);
            if (c.id.equals("")) {
                // Когда не найден фильм с таким ID - можно добавить фильм с этим ID
                FileDatabase.addCinema(cinema);
                // Получить обновлённый список фильмов
                cinemas = FileDatabase.getCinemas();
                // Показать обновлённый список фильмов
                cinemasAdapter.setNewList(cinemas);
                // Спрятать форму редактирования данных
                showEdit(false);
            }
        }
    }

    /***
     * Показать форму для создания нового сеанса
     */
    private void addNewSession() {
        // не режим редактирования
        session = new Session();
        // Очистить поля ввода для создания нового сеанса
        t_date_session.setText("");
        t_time_session.setText("");
        e_price_session.setText("");
        spin_cinema_session.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Hall.names));
        spin_cinema_session.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FileDatabase.getCinemasAsListString()));
        showEdit(true);
    }

    /***
     * Показать данные сеанса для редактирования
     * @param session редактируемый сеанс
     */
    private void editSessionData(Session session) {
        // режим редактирования
        this.session = session;
        // заполняем форму соответствующими данными
        t_date_session.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(session.date));
        t_time_session.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(session.date));
        e_price_session.setText(String.format(Locale.ENGLISH, "%.2f", session.price));
        ArrayList<String> names = FileDatabase.getCinemasAsListString();
        spin_cinema_session.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names));
        // получить номер фильма в списке
        int index = names.indexOf(FileDatabase.findCinemaById(session.cinema_id).name);
        // показать название фильма в выпадающем списке если он там найден
        if (index >= 0) spin_cinema_session.setSelection(index);
        // получить номер зала показа
        index = Hall.names.indexOf(session.hall);
        // показать название зала если он существует
        if (index >= 0) spin_hall_session.setSelection(index);
        showEdit(true);
    }

    /***
     * Удалить данные о сеансе
     * @param session удаляемый сеанс
     */
    private void removeSessionData(Session session) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление сеанса");
        builder.setMessage("Вы действительно желаете удалить этот сеанс '" +
                new SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault()).format(session.date) +
                "' из базы?");
        builder.setPositiveButton("Удалить", (dialog, which) -> {
            // Удалить сеанс из базы
            FileDatabase.removeSessionById(session.id);
            // Получить обновлённый список сеансов
            ArrayList<Session> sessions = FileDatabase.getSessions();
            // Показать обновлённый список сеансов
            sessionsAdapter.setNewList(sessions);
            showEdit(false);
        });
        builder.setNegativeButton("Отменить", (dialog, which) -> {
            // Просто закрыть диалог, как и при тапе за пределами окна диалога
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /***
     * Сохранить данные сеанса
     */
    private void saveSessionData() {
        String date = t_date_session.getText().toString().trim();
        String time = t_time_session.getText().toString().trim();
        String cinema_id = "";
        int index = spin_cinema_session.getAdapter().getCount();
        if (index >= 0) {
            cinema_id = cinemas.get(spin_cinema_session.getSelectedItemPosition()).id;
        }
        String price = e_price_session.getText().toString().trim();
        if (date.equals("") || time.equals("") || cinema_id.equals("") || price.equals("")) {
            Toast.makeText(this, "Все поля обязательны для заполнения", Toast.LENGTH_LONG).show();
        } else {
            // получить дату и время в милисекундах (путём парсинга)
            long datetime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .parse(date + " " + time, new ParsePosition(0)).getTime();
            // создаём новый или используем имеющийся идентификатор сеанса
            String id = "";
            if (session.id.equals("")) {
                // id пустой - создаётся новый сеанс
                id = genSessionID();
            } else {
                // id не пустой - редактируется существующий сеанс
                id = session.id;
                FileDatabase.removeSessionById(session.id);
            }
            // Создаём сеанс и указываем его данные
            session = new Session();
            session.id = id;
            session.hall = spin_hall_session.getSelectedItem().toString();
            session.date = datetime;
            session.cinema_id = cinema_id;
            session.price = Double.parseDouble(price);
            // Проверяем уникальность ID
            Session s = FileDatabase.findSessionById(session.id);
            if (s.id.equals("")) {
                // Когда не найден сеанс с таким ID - можно добавить сеанс с этим ID
                FileDatabase.addSession(session);
                // Получить обновлённый список сеансов
                sessions = FileDatabase.getSessions();
                // Показать обновлённый список сеансов
                sessionsAdapter.setNewList(sessions);
                // Спрятать форму редактирования данных
                showEdit(false);
            }
        }
    }

    /***
     * Сгенерировать новый уникальный идентификатор кассира
     * @return идентификатор
     */
    private String genUID() {
        Cashier u;
        String newUID;
        do {
            newUID = "id" + new Random().nextInt();
            u = FileDatabase.findCashierById(newUID);
        } while (!u.login.equals(""));
        // когда не нашёлся пользователь с таким идентификатором
        return newUID;
    }

    /***
     * Сгенерировать новый уникальный идентификатор фильма
     * @return идентификатор
     */
    private String genCinemaID() {
        Cinema c;
        String newUID;
        do {
            newUID = "id" + new Random().nextInt();
            c = FileDatabase.findCinemaById(newUID);
        } while (!c.id.equals(""));
        // когда не нашёлся фильм с таким идентификатором
        return newUID;
    }

    /***
     * Сгенерировать новый уникальный идентификатор сеанса
     * @return идентификатор
     */
    private String genSessionID() {
        Session s;
        String newUID;
        do {
            newUID = "id" + new Random().nextInt();
            s = FileDatabase.findSessionById(newUID);
        } while (!s.id.equals(""));
        // когда не нашёлся сеанс с таким идентификатором
        return newUID;
    }

    /***
     * Показать диалог выбора даты при клике
     * @param vg виджет на который нужно кликнуть для вызова
     * @param tv виджет отображения даты
     */
    private void bindDatePickerTo(View vg, TextView tv, String format) {
        vg.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Dialog dialog = new Dialog(v.getContext());
            DatePicker datePicker = new DatePicker(v.getContext());
            datePicker.setCalendarViewShown(true);
            datePicker.setSpinnersShown(false);
            datePicker.init(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String text = new SimpleDateFormat(format, Locale.getDefault())
                                    .format(calendar.getTimeInMillis());
                            tv.setText(text);
                        }
                    });
            dialog.setContentView(datePicker);
            // добавим кнопку для закрытия диалога
            Button button = new Button(v.getContext());
            button.setText("Ok");
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM | Gravity.END;
            button.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            dialog.addContentView(button, lp);
            // показать диалог
            dialog.show();
        });
    }

    /***
     * Показать диалог выбора времени при клике
     * @param vg виджет, на который нужно кликнуть для вызова
     * @param tv виджет отображения времени
     */
    private void bindTimePickerTo(View vg, TextView tv, String format) {
        vg.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Dialog dialog = new Dialog(v.getContext());
            TimePicker timePicker = new TimePicker(v.getContext());
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    // поместим полученное время в виджета в нужном формате
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    String text = new SimpleDateFormat(format, Locale.getDefault())
                            .format(calendar.getTimeInMillis());
                    tv.setText(text);
                }
            });
            dialog.setContentView(timePicker);
            // добавим кнопку для закрытия диалога
            Button button = new Button(v.getContext());
            button.setText("Ok");
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM | Gravity.END;
            button.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            dialog.addContentView(button, lp);
            // показать диалог
            dialog.show();
        });
    }

}