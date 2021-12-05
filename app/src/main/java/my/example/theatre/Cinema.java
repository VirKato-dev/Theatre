package my.example.theatre;

import androidx.annotation.NonNull;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/***
 * Характеристики фильма
 */
public class Cinema {

    /***
     * Идентификатор фильма
     */
    public String id = "";

    /***
     * Название фильма
     */
    public String name = "";

    /***
     * Длительность фильма
     */
    public long duration = 0L;

    /***
     * Описание фильма
     */
    public String description = "";

    /***
     * Задать длительность фильма в милисекундах из текста в формате "HH:mm"
     * без поправки на часвоой пояс устройства
     * @param str время в формате "HH:mm"
     */
    public void setDurationFromText(String str) {
        // перевести текст времени в число милисекунд
        long d = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .parse(str, new ParsePosition(0)).getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d);
        // получившееся время разбираем на составные части
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        // расчитываем длительность
        duration = hour * 3600000 + minute * 60000;
    }

    /***
     * Получить длительность фильма в формате "HH:mm"
     * @return длительность в формате "HH:mm"
     */
    public String getDurationAsText() {
        // используется количесвто милисекунд с поправкой на часовой пояс
        return String.format(Locale.ENGLISH,"%02d:%02d",duration / 3600000, duration % 60000);
    }

    /***
     * Получить длительность фильма как интервал задержки
     * @return интервал длительности
     */
    public long getDurationAsDelay() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(duration);
        // разбираем на составные части
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        // расчитываем длительность
        return hour * 3600000 + minute * 60000;
    }

    /***
     * Установить характеристики используя параметры из строки
     * @param str параметры в формате строки
     */
    public Cinema fromString(String str) {
        // разделить строку на элементы используя разделитель "|"
        String[] s = str.split("\\|");
        id = s[0];
        name = s[1];
        duration = Long.parseLong(s[2]);
        description = s[3];
        return this;
    }

    /***
     * Получить характеристики в виде строки параметров
     * toString - стандартный метод класса Object, используемый во многих операциях по-умолчанию.
     * @return параметры характеристик в виде строки
     */
    @NonNull
    @Override
    public String toString() {
        // объединить характеристики в строка используя разделитель "|"
        return id + "|" + name + "|" +
                String.format(Locale.US,"%d", duration) + "|" +
                description;
    }
}
