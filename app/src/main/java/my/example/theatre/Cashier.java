package my.example.theatre;

import androidx.annotation.NonNull;

/***
 * Характеристики пользователя (кассира)
 */
public class Cashier {

    /***
     * Логин пользователя
     */
    public String login = "";

    /***
     * Пароль пользователя
     */
    public String password = "";

    /***
     * Видимое имя пользователя
     */
    public String name = "";

    /***
     * Установить характеристики данного экземпляра используя строку параметров.
     * @param str параметры в формате одной строки
     * @return пользователь с установленными характеристиками
     */
    public Cashier fromString(String str) {
        // разделить строку на элементы используя разделитель "|"
        String[] s = str.split("\\|");
        login = s[0];
        password = s[1];
        name = s[2];
        return this;
    }

    /***
     * Получить характеристики экземпляра в формате одной строки для последующего храения в файле.
     * toString - стандартный метод класса Object, используемый во многих операциях по-умолчанию.
     * @return строка параметров
     */
    @NonNull
    @Override
    public String toString() {
        // объединить характеристики в строка используя разделитель "|"
        return login + "|" + password + "|" + name;
    }
}
