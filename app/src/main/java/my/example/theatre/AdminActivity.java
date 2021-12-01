package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/***
 * Экран администратора для регистрации кассиров, фильмов и сеансов показа
 */
public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }
}