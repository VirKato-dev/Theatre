package my.example.theatre;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/***
 * Экран кассира для оформлени покупки билетов
 */
public class CashierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
    }
}