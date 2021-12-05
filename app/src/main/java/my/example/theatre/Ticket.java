package my.example.theatre;

import androidx.annotation.NonNull;

import java.util.Locale;

/***
 * Билет на сеанс (Класс не используется. В процессе написания приложения,
 * был пересмотрен способ хранения информации о занятых местах в залах)
 */
public class Ticket {

    /***
     * Идентификатор билета
     */
    public String id = "";

    /***
     * Идентификатор сеанса
     */
    public String session_id = "";

    /***
     * Ряд
     */
    public int row = 0;

    /***
     * Место
     */
    public int chair = 0;

    /***
     * Установка параметров билета из строки данных
     * @return параметры билета в виде объекта класса
     */
    public Ticket fromString(String str) {
        // разделить строку на элементы используя разделитель "|"
        String[] s = str.split("\\|");
        id = s[0];
        session_id = s[1];
        row = Integer.parseInt(s[2]);
        chair = Integer.parseInt(s[3]);
        return this;
    }

    /***
     * Преобразование параметров билета в строку для удобного сохранения в файл
     * toString - стандартный метод класса Object, используемый во многих операциях по-умолчанию.
     * @return строка с параметрами билета
     */
    @NonNull
    @Override
    public String toString() {
        // объединить характеристики в строка используя разделитель "|"
        return  id + "|" + session_id + "|" +
                String.format(Locale.US,"%d", row) + "|" +
                String.format(Locale.US,"%d", chair);
    }

}
