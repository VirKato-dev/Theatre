package my.example.theatre;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

/***
 * Управление хранением и получением данных из базы данных (из файлов)
 */
public class FileDatabase {

    // файлы хранения данных по типам
    private static final String users_db = "users.db";
    private static final String cinemas_db = "cinemas.db";
    private static final String sessions_db = "sessions.db";
    private static final String hall_db = "hall.db";
    private static final String tickets_db = "tickets.db";

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
     * Получить список кассиров из базы
     * @return список кассиров из файла
     */
    public static ArrayList<Cashier> getCashiers() {
        ArrayList<Cashier> cashiers = new ArrayList<>();
        File file_from = new File(dir, users_db);
        try {
            if (file_from.exists()) {
                Reader reader = new FileReader(file_from);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n"); // разделитель строк
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    Cashier cashier = new Cashier().fromString(line);
                    cashiers.add(cashier);
                }
                reader.close();
                scanner.close();
            }
        } catch (IOException e) {
            Log.e("get cashiers", e.getMessage());
        }
        // сортируем список кассиров по полю ИМЯ
        Collections.sort(cashiers, (o1, o2) -> o1.name.compareToIgnoreCase(o2.name));
        return cashiers;
    }

    /***
     * Добавить кассира в файл
     * @param cashier кассир
     */
    public static void addUser(Cashier cashier) {
        File file = new File(dir, users_db);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // открыть файл в режиме добавления текста
            Writer fw = new FileWriter(file, true);
            String line = cashier + "\n";
            fw.write(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.e("add cashier", e.getMessage());
        }
    }

    /***
     * Поиск кассира в файле по идентификатору
     * @param login идентификатор уникален для каждого кассира
     * @return данные о кассире
     */
    public static Cashier findCashierById(String login) {
        // данные главного Админа системы
        Cashier cashier = new Cashier();
        cashier.login = "admin";
        cashier.password = "admin";
        cashier.name = "Хозяйка";
        if (!login.equals(cashier.login)) {
            // если это не главный админ системы, то
            cashier = new Cashier();
            // искать кассира в файле
            File file_from = new File(dir, users_db);
            try {
                if (!file_from.exists()) {
                    file_from.createNewFile();
                }
                // Подготавливаем Scanner для получения строк базы
                Reader reader = new FileReader(file_from);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n");
                while (scanner.hasNextLine()) {
                    // читаем очередную строку данных из файла
                    String line = scanner.nextLine();
                    cashier.fromString(line);
                    if (cashier.login.equals(login)) {
                        // прерываем цикл, чтобы данные найденного кассира ушли на обработку
                        break;
                    }
                    // очистить данные о ненужном кассира,
                    // чтобы данные последнего обработанного кассира не ушли на обработку
                    cashier = new Cashier();
                }
                // закрыть все использованные потоки
                reader.close();
                scanner.close();
            } catch (IOException e) {
                // обработка ошибок при работе с файлом
                // отладочная информация не выводится на основной экран приложения
                Log.e("find user", e.getMessage());
            }
        }
        // вернуть данные найденного кассира либо пустые данные
        return cashier;
    }

    /***
     * Удалить кассира из файла
     * @param login идентификатор кассира
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
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n"); // разделитель строк
            while (scanner.hasNextLine()) {
                // получить очередную строку данных
                String line = scanner.nextLine();
                Cashier cashier = new Cashier().fromString(line);
                if (!cashier.login.equals(login)) {
                    // если не тот кассир, которого нужно удалить,
                    // то записать его в новый файл
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            writer.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("remove cashier", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }


    /***
     * Получить список фильмов из базы
     * @return список фильмов
     */
    public static ArrayList<Cinema> getCinemas() {
        ArrayList<Cinema> cinemas = new ArrayList<>();
        File file_from = new File(dir, cinemas_db);
        try {
            if (file_from.exists()) {
                Reader reader = new FileReader(file_from);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n"); // разделитель строк
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    Cinema cinema = new Cinema().fromString(line);
                    cinemas.add(cinema);
                }
                reader.close();
                scanner.close();
            }
        } catch (IOException e) {
            Log.e("get cinemas", e.getMessage());
        }
        // сортируем фильмы по названию
        Collections.sort(cinemas, (o1, o2) -> o1.name.compareToIgnoreCase(o2.name));
        return cinemas;
    }

    /***
     * Получить список названий фильмов
     * @return названия фильмов
     */
    public static ArrayList<String> getCinemasAsListString() {
        ArrayList<String> list = new ArrayList<>();
        for (Cinema c : getCinemas()) {
            list.add(c.name);
        }
        return list;
    }

    /***
     * Добавить фильм в файл
     * @param cinema фильм
     */
    public static void addCinema(Cinema cinema) {
        File file = new File(dir, cinemas_db);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // открыть файл в режиме добавления текста
            Writer fw = new FileWriter(file, true);
            String line = cinema + "\n";
            fw.write(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.e("add cinema", e.getMessage());
        }
    }

    /***
     * Поиск фильма в файле по идентификатору
     * @param id идентификатор уникален для каждого фильма
     * @return данные о фильме
     */
    public static Cinema findCinemaById(String id) {
        Cinema cinema = new Cinema();
        // искать фильм в файле
        File file_from = new File(dir, cinemas_db);
        try {
            if (!file_from.exists()) {
                file_from.createNewFile();
            }
            // Подготавливаем Scanner для получения строк базы
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n");
            while (scanner.hasNextLine()) {
                // читаем очередную строку данных из файла
                String line = scanner.nextLine();
                cinema.fromString(line);
                if (cinema.id.equals(id)) {
                    // прерываем цикл, чтобы данные найденного фильма ушли на обработку
                    break;
                }
                // очистить данные о ненужном фильме, чтобы данные последнего обработанного фильма не ушли на обработку
                cinema = new Cinema();
            }
            // закрыть все использованные потоки
            reader.close();
            scanner.close();
        } catch (IOException e) {
            // обработка ошибок при работе с файлом
            // отладочная информация не выводится на основной экран приложения
            Log.e("find cinema", e.getMessage());
        }
        // вернуть данные найденного фильма либо пустые данные
        return cinema;
    }

    /***
     * Удалить фильм из файла
     * @param id идентификатор фильма
     */
    public static void removeCinemaById(String id) {
        // сделать копию файла для последующего обновления данных
        File file_from = new File(dir, cinemas_db + ".bak");
        File file_to = new File(dir, cinemas_db);
        if (file_to.exists()) file_to.renameTo(new File(dir, cinemas_db + ".bak"));
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
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n"); // разделитель строк
            while (scanner.hasNextLine()) {
                // получить очередную строку данных
                String line = scanner.nextLine();
                Cinema cinema = new Cinema().fromString(line);
                if (!cinema.id.equals(id)) {
                    // если не тот фильм, который нужно удалить, то записать его в новый файл
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            // закрыть все потоки
            writer.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("remove cinema", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }

    /***
     * Свободен ли указанный временной интервал от сеансов
     * @param start начало сеанса
     * @param duration продолжительность сеанса
     * @param hallName название зала
     * @param sessionId новый сеанс
     * @return да?
     */
    public static boolean isFreeTime(long start, long duration, String hallName, String sessionId) {
        // получить только дату в виде текста
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(start);
        ArrayList<Session> sessions = getSessions(date, hallName);

        for (Session s : sessions) {
            // длительность существующего сеанса
            if (!sessionId.equals("") && !sessionId.equals(s.id)) {
                // не проверяем существующий сеанс, если у него ID создаваемого сеанса
                long dur = FileDatabase.findCinemaById(s.cinema_id).getDurationAsDelay();
                if ((start >= s.date && start <= s.date + dur) ||
                        (start + duration >= s.date && start + duration <= s.date + dur)) {
                    // если границы нового сеанса пересеклись с границами уже существующих сеансов,
                    // то сохранять текущий сеанс нельзя из-за временной накладки сеансов
                    return false;
                }
            }
        }
        // ниодной временной накладки с существующими сеансами не обнаружено
        return true;
    }

    /***
     * Получить список сеансов из базы
     * @return список сеансов
     */
    public static ArrayList<Session> getSessions() {
        ArrayList<Session> sessions = new ArrayList<>();
        File file_from = new File(dir, sessions_db);
        try {
            if (file_from.exists()) {
                Reader reader = new FileReader(file_from);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n"); // разделитель строк
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    Session session = new Session().fromString(line);
                    sessions.add(session);
                }
                reader.close();
                scanner.close();
            }
        } catch (IOException e) {
            Log.e("get sessions", e.getMessage());
        }
        // сортируем сеансы по названию
        Collections.sort(sessions, (o1, o2) -> (int) (o1.date - o2.date));
        return sessions;
    }

    /***
     * Получить список сеансов на указанную дату из базы
     * @param date дата сеансов
     * @return список сеансов
     */
    public static ArrayList<Session> getSessions(String date) {
        ArrayList<Session> sessions = new ArrayList<>();
        File file_from = new File(dir, sessions_db);
        try {
            if (file_from.exists()) {
                Reader reader = new FileReader(file_from);
                Scanner scanner = new Scanner(reader);
                scanner.useDelimiter("\n"); // разделитель строк
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    Session session = new Session().fromString(line);
                    if (date.equals(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(session.date))) {
                        sessions.add(session);
                    }
                }
                reader.close();
                scanner.close();
            }
        } catch (IOException e) {
            Log.e("get sessions", e.getMessage());
        }
        // сортируем сеансы по названию
        Collections.sort(sessions, (o1, o2) -> (int) (o1.date - o2.date));
        return sessions;
    }

    /***
     * Получить список сеансов на указанную дату и для указанного зала из базы
     * @param date дата сеансов
     * @param hallName название зала
     * @return список сеансов
     */
    public static ArrayList<Session> getSessions(String date, String hallName) {
        ArrayList<Session> sessions = getSessions(date);
        Iterator<Session> it = sessions.iterator();
        while (it.hasNext()) {
            Session s = it.next();
            if (!s.hall.equals(hallName)) it.remove();
        }
        return sessions;
    }

    /***
     * Добавить сеанс в файл
     * @param session сеанс
     */
    public static void addSession(Session session) {
        File file = new File(dir, sessions_db);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // открыть файл в режиме добавления текста
            Writer fw = new FileWriter(file, true);
            String line = session + "\n";
            fw.write(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.e("add session", e.getMessage());
        }
    }

    /***
     * Поиск сеанса в файле по идентификатору
     * @param id идентификатор уникален для каждого сеанса
     * @return данные о сеансе
     */
    public static Session findSessionById(String id) {
        Session session = new Session();
        // искать сеанс в файле
        File file_from = new File(dir, sessions_db);
        try {
            if (!file_from.exists()) {
                file_from.createNewFile();
            }
            // Подготавливаем Scanner для получения строк базы
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n");
            while (scanner.hasNextLine()) {
                // читаем очередную строку данных из файла
                String line = scanner.nextLine();
                session.fromString(line);
                if (session.id.equals(id)) {
                    // прерываем цикл, чтобы данные найденного сеанса ушли на обработку
                    break;
                }
                // очистить данные о ненужном сеансе, чтобы данные последнего обработанного сеанса не ушли на обработку
                session = new Session();
            }
            // закрыть все использованные потоки
            reader.close();
            scanner.close();
        } catch (IOException e) {
            // отладочная информация не выводится на основной экран приложения
            Log.e("find session", e.getMessage());
        }
        // вернуть данные найденного сеанса либо пустые данные
        return session;
    }

    /***
     * Удалить сеанс из файла
     * @param id идентификатор сеанс
     */
    public static void removeSessionById(String id) {
        // сделать копию файла для последующего обновления данных
        File file_from = new File(dir, sessions_db + ".bak");
        File file_to = new File(dir, sessions_db);
        if (file_to.exists()) file_to.renameTo(new File(dir, sessions_db + ".bak"));
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
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n"); // разделитель строк
            while (scanner.hasNextLine()) {
                // получить очередную строку данных
                String line = scanner.nextLine();
                Session session = new Session().fromString(line);
                if (!session.id.equals(id)) {
                    // если не тот сеанс, который нужно удалить, то записать его в новый файл
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            // закрыть все потоки
            writer.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("remove session", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }

    /***
     * Получить состояние залов на указанную дату
     * @param datetime дата и время показа
     * @return залы
     */
    public static Hall getHallForDate(long datetime) {
        Hall hall = new Hall();
        // искать залы в файле
        File file_from = new File(dir, hall_db);
        try {
            if (!file_from.exists()) {
                file_from.createNewFile();
            }
            // Подготавливаем Scanner для получения строк базы
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n");
            while (scanner.hasNextLine()) {
                // читаем очередную строку данных из файла
                String line = scanner.nextLine();
                hall.fromString(line);
                if (hall.date == datetime) {
                    // прерываем цикл, чтобы данные найденных залов ушли на обработку
                    break;
                }
                // очистить данные о ненужных залах, чтобы последние ненужные данные не ушли на обработку
                hall = new Hall();
            }
            // закрыть все использованные потоки
            reader.close();
            scanner.close();
        } catch (IOException e) {
            // отладочная информация не выводится на основной экран приложения
            Log.e("find hall", e.getMessage());
        }
        return hall;
    }

    /***
     * Удалить залы на указанную дату
     * @param date дата
     */
    public static void removeHallForDate(long date) {
        // сделать копию файла для последующего обновления данных
        File file_from = new File(dir, hall_db + ".bak");
        File file_to = new File(dir, hall_db);
        if (file_to.exists()) file_to.renameTo(new File(dir, hall_db + ".bak"));
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
            Reader reader = new FileReader(file_from);
            Scanner scanner = new Scanner(reader);
            scanner.useDelimiter("\n"); // разделитель строк
            while (scanner.hasNextLine()) {
                // получить очередную строку данных
                String line = scanner.nextLine();
                Hall hall = new Hall();
                hall.fromString(line);
                if (hall.date != date) {
                    // если не та дата, которую нужно удалить, то записать залы в новый файл
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            // закрыть все потоки
            writer.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            Log.e("remove hall", e.getMessage());
        }
        // удалить старую версию файла
        file_from.delete();
    }

    /***
     * Добавить зал в файл
     * @param hall сеанс
     */
    public static void addHall(Hall hall) {
        File file = new File(dir, hall_db);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // открыть файл в режиме добавления текста
            Writer fw = new FileWriter(file, true);
            String line = hall + "\n";
            fw.write(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.e("add hall", e.getMessage());
        }
    }


}
