package my.example.theatre;

import androidx.annotation.NonNull;

/***
 * Характеристики фильма или мультика
 */
public class Cinema {

    /***
     * Идентификатор фильма, мультика
     */
    public String id = "";

    /***
     * Название фильма, мультика
     */
    public String name = "";

    /***
     * Описание фильма, мультика
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
        description = s[2];
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
        return id + "|" + name + "|" + description;
    }
}
