package my.example.theatre;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private EditText t_price_session;
    private Button b_save_session;
    private RecyclerView rv_sessions;
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
    private EditText e_cinema_description;
    private Button b_save_cinema;
    private RecyclerView rv_cinemas;

    // кнопка добавления
    private FloatingActionButton fab_add_data;

    // нижние кнопки переключения режимов
    private LinearLayout l_bottom_buttons;
    private Button b_cashier;
    private Button b_cinema;
    private Button b_session;

    // переменные алгоритма
    private User user = new User();
    private ArrayList<User> cashiers = new ArrayList<>();
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
        // форма редактирования сеанса
        l_session_edit = findViewById(R.id.l_session_edit);
        spin_hall_session = findViewById(R.id.spin_hall_session);
        l_date_session = findViewById(R.id.l_date_session);
        t_date_session = findViewById(R.id.t_date_session);
        l_time_session = findViewById(R.id.l_time_session);
        t_time_session = findViewById(R.id.t_time_session);
        t_price_session = findViewById(R.id.t_price_session);
        b_save_session = findViewById(R.id.b_save_session);
        b_save_session.setOnClickListener(v -> {
            showScreen(2);
        });
        rv_sessions = findViewById(R.id.rv_sessions);

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
        cashiers = FileDatabase.getUsers();
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
        b_save_cinema = findViewById(R.id.b_save_cinema);
        b_save_cinema.setOnClickListener(v -> {
            showScreen(1);
        });
        rv_cinemas = findViewById(R.id.rv_cinemas);

        // кнопка добавления
        fab_add_data = findViewById(R.id.fab_add_data);
        fab_add_data.setOnClickListener(v -> {
            addNewCashier();
            showEdit(true);
        });

        // нижние кнопки переключения режимов
        l_bottom_buttons = findViewById(R.id.l_bottom_buttons);
        b_cashier = findViewById(R.id.b_cashier);
        b_cinema = findViewById(R.id.b_cinema);
        b_session = findViewById(R.id.b_session);

        showScreen(0);
    }

    /***
     * Показать макет экрана соответствующий текущему режиму редактирования данных
     */
    private void showScreen(int page) {
        screenNumber = page;
        // откроем только список выбранного режима
        l_cashier_edit.setVisibility(View.GONE);
        rv_cashiers.setVisibility(0 == page ? View.VISIBLE : View.GONE);
        l_cinema_edit.setVisibility(View.GONE);
        rv_cinemas.setVisibility(0 == page ? View.VISIBLE : View.GONE);
        l_session_edit.setVisibility(View.GONE);
        rv_sessions.setVisibility(0 == page ? View.VISIBLE : View.GONE);
        // спрячем панель редактирования данных
        showEdit(false);
    }

    /***
     * Показать форму редактирования данных
     * @param show показать?
     */
    private void showEdit(boolean show) {
        switch (screenNumber) {
            case 0:
                l_cashier_edit.setVisibility(show ? View.VISIBLE : View.GONE);
                break;
            case 1:
                l_cinema_edit.setVisibility(show ? View.VISIBLE : View.GONE);
                break;
            case 2:
                l_session_edit.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        // спрячем плавающую кнопку добавления новых данных (справа снизу)
        fab_add_data.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /***
     * Показать форму для создания нового посетителя
     */
    private void addNewCashier() {
        // не режим редактирования
        user = new User();
        // Очистить поля ввода для создания нового посетителя
        e_cashier_name.setText("");
        e_cashier_login.setText("");
        e_cashier_password.setText("");
    }

    /***
     * Показать данные кассира для редактирования
     * @param user редактируемый кассир
     */
    private void editCashierData(User user) {
        // режим редактирования
        this.user = user;
        fab_add_data.setVisibility(View.GONE);
        // заполняем форму соответствующими данными
        l_cashier_edit.setVisibility(View.VISIBLE);
        e_cashier_login.setText(user.login);
        e_cashier_password.setText(user.password);
        e_cashier_name.setText(user.name);
    }

    /***
     * Удалить данные о кассире
     * @param user удаляемый кассир
     */
    private void removeCashierData(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление кассира");
        builder.setMessage("Вы действительно желаете удалить этого кассира '" + user.name + "' из базы?");
        builder.setPositiveButton("Удалить", (dialog, which) -> {
            // Удалить кассира из базы
            FileDatabase.removeUserById(user.login);
            // Получить обновлённый список кассиров
            ArrayList<User> cashiers = FileDatabase.getUsers();
            // Показать обновлённый список кассиров
            cashiersAdapter.setNewList(cashiers);
            l_cashier_edit.setVisibility(View.GONE);
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
//            if (!user.login.equals("")) {
//                // user здесь хранит данные о кассире выбранном из списка или ничего.
//                // ID не пустой - режим редактирования.
//                login = user.login;
//                // Перед внесением изменений в базу нужно удалить старые данные из базы.
//                FileDatabase.removeUserById(login);
//                // Потому что наша база не умеет обновлять имеющиеся данные.
//            } else {
//                // Кассир без данных - режим создания.
//                // Выдать новый идентификатор
//                login = generateUID();
//            }
            FileDatabase.removeUserById(login);
            // Создаём человека и указываем его данные
            user = new User();
            user.login = login;
            user.password = password;
            user.name = name;
            // Проверяем уникальность логина
            User h = FileDatabase.findUserById(user.login);
            if (h.login.equals("")) {
                // Когда не найден кассир с таким ID - можно добавить кассира с этим логином
                FileDatabase.addOperator(user);
                // Получить обновлённый список кассиров
                cashiers = FileDatabase.getUsers();
                // Показать обновлённый список кассиров
                cashiersAdapter.setNewList(cashiers);
                // Спрятать форму редактирования данных
                showEdit(false);
            }
        }
    }

    /***
     * Сгенерировать новый уникальный идетификатор
     * @return идентификатор
     */
    private String generateUID() {
        User u;
        String newUID;
        do {
            newUID = "id" + new Random().nextInt();
            u = FileDatabase.findUserById(newUID);
            Log.e("generateID", u.login);
        } while (!u.login.equals(""));
        // когда не нашёлся пользователь с таким идентификатором
        return newUID;
    }

}