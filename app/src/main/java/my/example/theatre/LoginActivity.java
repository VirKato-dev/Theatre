package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/***
 * Экран авторизации пользователя одинаков для админа и кассира
 */
public class LoginActivity extends AppCompatActivity {

    // список виджетов для формы авторизации, к которым необходим доступ из кода приложения
    private EditText e_login;
    private EditText e_password;
    private Button b_login;

    private String login = "";
    private String password = "";

    private Cashier cashier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // виджеты макета экрана
        e_login = findViewById(R.id.e_login);
        e_password = findViewById(R.id.e_password);
        b_login = findViewById(R.id.b_login);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRole();
            }
        });
    }

    /***
     * Проверить роль пользователя и отправить на соответствующий экран приложения
     */
    private void checkRole() {
        // получаем введённый текст без крайних пробелов
        login = e_login.getText().toString().trim();
        password = e_password.getText().toString().trim();

        if ("".equals(login) || "".equals(password)) {
            Toast.makeText(this, "Поля 'Логин' и 'Пароль' должны быть заполнены", Toast.LENGTH_LONG).show();
        } else {
            // ищем пользователя по базе данных
            cashier = FileDatabase.findUserById(login);
            // user будет обязательно иметь какие-то данные (!=null)

            if (cashier.login.equals(login) && cashier.password.equals(password)) {
                // если найден пользователь с указанными логином и паролем,
                // то перейти на соответствующий экран
                if (cashier.login.equals("admin")) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    startActivity(new Intent(this, CashierActivity.class));
                }
                Toast.makeText(this, "Добро пожаловать, " + cashier.name + "!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Такого пользователя не существует.", Toast.LENGTH_LONG).show();
            }

            // очистить поля ввода
            e_login.setText("");
            e_password.setText("");
        }
    }

}