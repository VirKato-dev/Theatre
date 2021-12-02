package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


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
        });
        rv_cashiers = findViewById(R.id.rv_cashiers);
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
    private void showScreen(int numberOfScreen) {
        // сначала скроем все формы ввода и списки
        l_cashier_edit.setVisibility(View.GONE);
        rv_cashiers.setVisibility(View.GONE);
        l_cinema_edit.setVisibility(View.GONE);
        rv_cinemas.setVisibility(View.GONE);
        l_session_edit.setVisibility(View.GONE);
        rv_sessions.setVisibility(View.GONE);
        // откроем только список выбранного режима
        switch (numberOfScreen) {
            case 0:
                rv_cashiers.setVisibility(View.VISIBLE);
                break;
            case 1:
                rv_cinemas.setVisibility(View.VISIBLE);
                break;
            case 2:
                rv_sessions.setVisibility(View.VISIBLE);
        }
        // покажем плавающую кнопку добавления новых данных (справа снизу)
        fab_add_data.setVisibility(View.VISIBLE);
    }


}