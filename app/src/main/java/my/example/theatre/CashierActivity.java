package my.example.theatre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/***
 * Экран кассира для оформлени покупки билетов
 */
public class CashierActivity extends AppCompatActivity {

    private Spinner spin_hall_ticket;
    private TextView t_date_ticket;

    private LinearLayout l_chairs_cashier;
    private RecyclerView rv_chairs;
    private ChairsAdapter chairsAdapter;

    private RecyclerView rv_sessions_cashier;
    private SessionsAdapter sessionsAdapter;

    private Hall hall;
    private ArrayList<Boolean> chairs = new ArrayList<>();

    private Session session;
    private ArrayList<Session> sessions;

    private long datetime = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        setTitle("Кассир");

        // виджеты выбора Зала и Даты сеанса
        spin_hall_ticket = findViewById(R.id.spin_hall_ticket);
        t_date_ticket = findViewById(R.id.t_date_ticket);

        // адаптер списка Залов
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Hall.names);
        spin_hall_ticket.setAdapter(spinAdapter);
        // когда изменился/выбран Зал просмотра
        spin_hall_ticket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!t_date_ticket.getText().toString().equals("")) {
                    // если дата сеанса указана
                    showSessions(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bindDatePickerTo(t_date_ticket, t_date_ticket, "dd/MM/yyyy");

        rv_chairs = findViewById(R.id.rv_chairs);

        l_chairs_cashier = findViewById(R.id.l_chairs_cashier);
        rv_sessions_cashier = findViewById(R.id.rv_sessions_cashier);

        showSessions(false);

    }

    /***
     * Показать список сеансов на указанную дату
     * @param isShow
     */
    private void showSessions(boolean isShow) {
        l_chairs_cashier.setVisibility(View.GONE);
        rv_sessions_cashier.setVisibility(isShow ? View.VISIBLE : View.GONE);
        String date = t_date_ticket.getText().toString();
        sessions = FileDatabase.getSessions(date, spin_hall_ticket.getSelectedItem().toString());

        sessionsAdapter = new SessionsAdapter(sessions);
        // задать способ взаимного расположения элементов списка на экране (Вертикальный)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_sessions_cashier.setLayoutManager(linearLayoutManager);
        sessionsAdapter.setClickListener((view, position) -> {
            showChairs(sessionsAdapter.getItem(position));
        });
        // задать обработчик долгого нажатия на элемент списка
        sessionsAdapter.setLongClickListener((view, position) -> {
        });
        // связываем виджет списка с адаптером данных списка
        rv_sessions_cashier.setAdapter(sessionsAdapter);
    }

    /***
     * Меню справа сверху
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Выйти из аккаунта");
        return super.onCreateOptionsMenu(menu);
    }

    /***
     * Сработает при выборе любого пункта меню (справа сверху)
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
                // когда согласились с выбранной датой, произойдёт обновление информации о свободных местах
                showSessions(true);
                dialog.dismiss();
            });
            dialog.addContentView(button, lp);
            // показать диалог
            dialog.show();
        });
    }

    /***
     * Показать места для зрителей выбранного сеанса
     */
    private void showChairs(Session session) {
        l_chairs_cashier.setVisibility(View.VISIBLE);
        datetime = session.date;

        hall = FileDatabase.getHallForDate(datetime);
        getChairs();

        chairsAdapter = new ChairsAdapter(chairs);
        // задать способ взаимного расположения элементов списка на экране (Сетка)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, Hall.redHallCol);
        rv_chairs.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        chairsAdapter.setClickListener((view, position) -> {
            changeStateOfChair(datetime, position);
        });
        // задать обработчик долгого нажатия на элемент списка
        chairsAdapter.setLongClickListener((view, position) -> {
        });
        // связываем виджет списка с адаптером данных списка
        rv_chairs.setAdapter(chairsAdapter);
    }

    private void changeStateOfChair(long date, int pos) {
        // заменить значение на противоположное
        boolean[] c;
        if (spin_hall_ticket.getSelectedItemPosition() == 0) {
            c = hall.redChair;
        } else {
            c = hall.blueChair;
        }
        c[pos] = !c[pos];
        FileDatabase.removeHallForDate(date);
        hall.date = date;
        FileDatabase.addHall(hall);
        // показать новое состояние мест
        getChairs();
        chairsAdapter.setNewList(chairs);
    }

    /***
     * Получить состояние мест на указанную дату в зависимости от выбранного зала
     */
    private void getChairs() {
        boolean[] c;
        chairs.clear();
        // взять места только выбранного зала
        if (spin_hall_ticket.getSelectedItemPosition() == 0) {
            c = hall.redChair;
        } else {
            c = hall.blueChair;
        }
        // перегнать массив в список с помощью итератора
        for (boolean b : c) {
            chairs.add(b);
        }

    }

}