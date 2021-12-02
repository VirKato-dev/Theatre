package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
        rv_sessions = findViewById(R.id.rv_sessions);
        // форма редактирования кассира
        l_cashier_edit = findViewById(R.id.l_cashier_edit);
        e_cashier_name = findViewById(R.id.e_cashier_name);
        e_cashier_login = findViewById(R.id.e_cashier_login);
        e_cashier_password = findViewById(R.id.e_cashier_password);
        b_save_cashier = findViewById(R.id.b_save_cashier);
        rv_cashiers = findViewById(R.id.rv_cashiers);
        // форма редактирования фильма
        l_cinema_edit = findViewById(R.id.l_cinema_edit);
        e_cinema_name = findViewById(R.id.e_cinema_name);
        e_cinema_description = findViewById(R.id.e_cinema_description);
        b_save_cinema = findViewById(R.id.b_save_cinema);
        rv_cinemas = findViewById(R.id.rv_cinemas);
        // кнопка добавления
        fab_add_data = findViewById(R.id.fab_add_data);
        // нижние кнопки переключения режимов
        l_bottom_buttons = findViewById(R.id.l_bottom_buttons);

    }


}