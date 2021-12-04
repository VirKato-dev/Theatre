package my.example.theatre;

import androidx.annotation.NonNull;

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
