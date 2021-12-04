package my.example.theatre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/***
 * Экран кассира для оформлени покупки билетов
 */
public class CashierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        setTitle("Кассир");

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

}