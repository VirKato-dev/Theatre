package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/***
 * Экран авторизации пользователя одинаков для админа и кассира
 */
public class LoginActivity extends AppCompatActivity {

    // список виджетов для формы авторизации, к которым необходим доступ из кода приложения
    private EditText e_login;
    private EditText e_password;
    private CheckBox cb_save_me;
    private Button b_login;

    private String login = "";
    private String password = "";

    private Cashier cashier;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("data", MODE_PRIVATE);

        // виджеты макета экрана
        e_login = findViewById(R.id.e_login);
        e_password = findViewById(R.id.e_password);
        cb_save_me = findViewById(R.id.cb_save_me);
        b_login = findViewById(R.id.b_login);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRole();
            }
        });

        // а не авторизовалмя ли пользователь ранее?
        if (!sp.getString("login", "").equals("")) {
            // авторизовался ранее
            login = sp.getString("login", "");
            password = sp.getString("password", "");
            canGo(login, password);
        }
    }

    /***
     * Проверить роль пользователя и отправить на соответствующий экран приложения
     */
    private void checkRole() {
        // получаем введённый текст (логин и пароль) без крайних пробелов
        login = e_login.getText().toString().trim();
        password = e_password.getText().toString().trim();

        canGo(login, password);

        // очистить поля ввода
        e_login.setText("");
        e_password.setText("");
    }

    /***
     * можно ли пройти дальше
     */
    private void canGo(String login, String password) {
        if ("".equals(login) || "".equals(password)) {
            Toast.makeText(this, "Поля 'Логин' и 'Пароль' должны быть заполнены", Toast.LENGTH_LONG).show();
        } else {
            // ищем пользователя по базе данных
            cashier = FileDatabase.findCashierById(login);
            // cashier будет обязательно иметь какие-то данные (!=null)
            if (cashier.login.equals(login) && cashier.password.equals(password)) {
                // если найден пользователь с указанными логином и паролем,
                // то перейти на соответствующий экран
                if (cashier.login.equals("admin")) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    startActivity(new Intent(this, CashierActivity.class));
                }
                Toast.makeText(this, "Добро пожаловать, " + cashier.name + "!", Toast.LENGTH_LONG).show();
                if (cb_save_me.isChecked()) {
                    // сохраним информацию о том, что пользователь уже вошёл в систему
                    sp.edit().putString("login", cashier.login).apply();
                    sp.edit().putString("password", cashier.password).apply();
                } else {
                    sp.edit().remove("login").apply();
                    sp.edit().remove("password").apply();
                }
                finish();
            } else {
                Toast.makeText(this, "Такого пользователя не существует.", Toast.LENGTH_LONG).show();
            }
        }
    }

}