package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // производим первичную подготовку базы данных
        FileDatabase.initialize(getApplicationContext());

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}