package my.example.theatre;

import java.util.ArrayList;
import java.util.Arrays;

/***
 * Состояние залов на указанную дату
 */
public class Hall {

    // static - переменная хранит своё значение для всех экземпляров этого (Hall) класса
    // final - переменная не может быть изменена

    /***
     * Названия залов просмотра
     */
    public final static ArrayList<String> names = new ArrayList<>(Arrays.asList("Красный", "Синий"));

    /***
     * Дата состояния залов
     */
    public long date = 0L;

    /***
     * рядов в красном зале
     */
    public static int redHallRow = 5;

    /***
     * мест в каждом ряду красного зала
     */
    public static int redHallCol = 15;

    /***
     * состояние мест в красном зале
     */
    public boolean[] redChair = new boolean[redHallRow * redHallCol];

    /***
     * рядов в синем зале
     */
    public static int blueHallRow = 5;

    /***
     * мест в каждом ряду синего зала
     */
    public static int blueHallCol = 15;

    /***
     * состояние мест в синем зале
     */
    public boolean[] blueChair = new boolean[blueHallRow * blueHallCol];

    /***
     * Создать информацию о занятости мест на указанную дату
     */
    public Hall() {
        // все места свободны во всех залах
        for (int r = 0; r < redHallRow * redHallCol; r++) {
            redChair[r] = false;
        }
        for (int r = 0; r < blueHallRow * blueHallCol; r++) {
            blueChair[r] = false;
        }
    }

    /***
     * Задать параметры занятости мест в залах в соответствии со значениями в полученой строке
     * @param str строка параметров
     */
    public void fromString(String str) {
        // разбить строчку на элементы
        String[] val = str.split("\\|");
        if (val.length > 0) {
            date = Long.parseLong(val[0]);
            int index = 0;
            // задать состояние занятости стульев Красного Зала
            for (int r = 0; r < redHallRow * redHallCol; r++) {
                index++;
                // если index вышел за пределы массива полученных данных
                // значит все остальные стулья свободны
                redChair[r] = false;
                if (val.length > index) {
                    // если "*" значит место занято
                    redChair[r] = val[index].equals("*");
                }
            }
            // продолжить процедуру для Синего Зала
            for (int r = 0; r < blueHallRow * blueHallCol; r++) {
                index++;
                // если index вышел за пределы массива полученных данных
                // значит все остальные стулья свободны
                blueChair[r] = false;
                if (val.length > index) {
                    // если "*" значит место занято
                    blueChair[r] = val[index].equals("*");
                }
            }
        }
    }

    /***
     * Сохранить состояние мест в строке для последующего хранения в файле базы
     * @return строка состояния мест в залах
     */
    @Override
    public String toString() {
        String str = date + "|";
        for (int r = 0; r < redHallRow * redHallCol; r++) {
            // если место занято, то сохраняем *, иначе ничего
            str += (redChair[r] ? "*" : "") + "|";
        }
        for (int r = 0; r < blueHallRow * blueHallCol; r++) {
            // если место занято, то сохраняем *, иначе ничего
            str += (blueChair[r] ? "*" : "") + "|";
        }
        return str;
    }
}
