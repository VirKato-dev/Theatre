package my.example.theatre;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            // искать пользователя в файле
            File file_from = new File(dir, users_db);
            try {
                if (!file_from.exists()) {
                    file_from.createNewFile();
                }
                // Подготавливаем Scanner для получения строк базы
//                InputStream fis = new FileInputStream(file);
//                Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                FileReader reader = new FileReader(file_from);
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
                scanner.close();
                reader.close();
//                fis.close();
            } catch (IOException e) {
                // обработка ошибок при работе с файлом
                // отладочная информация не выводится на основной экран приложения
                Log.e("REPOSITORY find", e.getMessage());
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

//            InputStream fis = new FileInputStream(file_from);
//            Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            FileReader reader = new FileReader(file_from);
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
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("REPOSITORY remove", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }


}
