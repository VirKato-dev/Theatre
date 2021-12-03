package my.example.theatre;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/***
 * Управление хранением и получением данных из базы данных (из файлов)
 */
public class FileDatabase {

    // файлы хранения данных по типам
    private static final String users_db = "users.db";
    private static final String tickets_db = "tickets.db";
    private static final String sessions_db = "sessions.db";
    private static final String cinemas_db = "cinemas.db";

    /***
     * Первичная настройка папки для хранения файлов.
     */
    private static File dir = new File("");

    /***
     * Инициализация папки для хранения файлов
     * "/Android/data/my.example.theatre/cache/"
     * @param context контекст приложения/активности
     */
    public static void initialize(Context context) {
        dir = context.getExternalCacheDir();
    }

    /***
     * Получить список людей из базы
     * @return список людей из файла
     */
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(dir, users_db);
        try {
            if (file.exists()) {
                InputStream fis = new FileInputStream(file);
                Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n"); // разделитель строк
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    User user = new User().fromString(line);
                    users.add(user);
                }
                fis.close();
                reader.close();
                scanner.close();
            }
        } catch (IOException e) {
            Log.e("get users", e.getMessage());
        }
        Log.e("get users", users.toString());
        return users;
    }

    /***
     * Добавить пользователя в файл
     * @param user пользователь
     */
    public static void addOperator(User user) {
        File file = new File(dir, users_db);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // открыть файл в режиме добавления текста
            Writer fw = new FileWriter(file, true);
            String line = user + "\n";
            fw.write(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.e("add user", e.getMessage());
        }
    }

    /***
     * Поиск пользователя в файле по идентификатору
     * @param login идентификатор уникален для каждого пользователя
     * @return данные о пользователе
     */
    public static User findUserById(String login) {
        // данные главного Админа системы
        User user = new User();
        user.login = "admin";
        user.password = "admin";
        user.name = "мой Хозяин";

        if (!login.equals(user.login)) {
            // если это не главный админ системы, то
            user = new User();
            // искать пользователя в файле
            File file_from = new File(dir, users_db);
            try {
                if (!file_from.exists()) {
                    file_from.createNewFile();
                }
                // Подготавливаем Scanner для получения строк базы
                InputStream fis = new FileInputStream(file_from);
                Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n");

                while (scanner.hasNextLine()) {
                    // читаем очередную строку данных из файла
                    String line = scanner.nextLine();
                    user.fromString(line);
                    if (user.login.equals(login)) {
                        // прерываем цикл, чтобы данные найденного пользователя ушли на обработку
                        break;
                    }
                    // очистить данные о ненужном пользователе,
                    // чтобы данные последнего обработанного пользователя не ушли на обработку
                    user = new User();
                }

                // закрыть все использованные потоки
                fis.close();
                reader.close();
                scanner.close();
            } catch (IOException e) {
                // обработка ошибок при работе с файлом
                // отладочная информация не выводится на основной экран приложения
                Log.e("find user", e.getMessage());
            }
        }
        // вернуть данные найденного пользователя либо пустые данные
        return user;
    }

    /***
     * Удалить пользователя из файла
     * @param login идентификатор пользователя
     */
    public static void removeUserById(String login) {
        // сделать копию файла для последующего обновления данных
        File file_from = new File(dir, users_db + ".bak");
        File file_to = new File(dir, users_db);
        if (file_to.exists()) file_to.renameTo(new File(dir, users_db + ".bak"));

        try {
            if (!file_from.exists()) {
                // для правильной работы нам требуется наличие файла-источника (даже пустого)
                file_from.createNewFile();
            }
            if (!file_to.exists()) {
                // требуется наличие файла-приёмника
                file_to.createNewFile();
            }

            // запись в файла будет производится в режиме дополнения информации
            FileWriter writer = new FileWriter(file_to, true);
            InputStream fis = new FileInputStream(file_from);
            Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n"); // разделитель строк

            while (scanner.hasNextLine()) {
                // получить очередную строку данных
                String line = scanner.nextLine();
                User user = new User().fromString(line);

                if (!user.login.equals(login)) {
                    // если не тот пользователь, которого нужно удалить,
                    // то записать его в новый файл
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            writer.close();
            fis.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("remove user", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }


}
