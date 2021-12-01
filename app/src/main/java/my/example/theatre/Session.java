package my.example.theatre;

import androidx.annotation.NonNull;

import java.util.Locale;

/***
 * Сеанс показа фильма, мультика
 */
public class Session {

    /***
     * Идентификатор сеанса
     */
    public String id = "";

    /***
     * Зал показа
     */
    public String hall = "";

    /***
     * Идентификатор фильма
     */
    public String cinema_id = "";

    /***
     * День и время начала показа
     */
    public long date = 0L;

    /***
     * Длительность показа
     */
    public long duration = 0L;

    /***
     * Стоимость сеанса
     */
    public double price = 0d;

    /***
     * Установка параметров сеанса из строки данных
     * @return параметры сеанса в виде объекта класса
     */
    public Session fromString(String str) {
        // разделить строку на элементы используя разделитель "|"
        String[] s = str.split("\\|");
        id = s[0];
        hall = s[1];
        cinema_id = s[2];
        date = Long.parseLong(s[3]);
        duration = Long.parseLong(s[4]);
        price = Double.parseDouble(s[5]);
        return this;
    }

    /***
     * Преобразование параметров сеанса в строку для удобного сохранения в файл
     * toString - стандартный метод класса Object, используемый во многих операциях по-умолчанию.
     * @return строка с параметрами сеанса
     */
    @NonNull
    @Override
    public String toString() {
        // объединить характеристики в строка используя разделитель "|"
        return  id + "|" + hall + "|" + cinema_id + "|" +
                String.format(Locale.US,"%d", date) + "|" +
                String.format(Locale.US,"%d", duration) + "|" +
                String.format(Locale.US,"%.2f", price) + "|" + hall;
    }
}
