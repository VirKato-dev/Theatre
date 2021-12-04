package my.example.theatre;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ViewHolder> {

    /***
     * Ссылка на привязанный список сеансов
     */
    private ArrayList<Session> data;

    /***
     * Инструмент позволяющий сделать из XML-файла виджет для последующего вывода на экран
     */
    private LayoutInflater inflater;

    /***
     * Интерфейс для передачи сигнала о том, что элемент видимого списка был кликнут
     */
    private ItemClickListener clickListener;

    /***
     * Интерфейс для передачи сигнала о том, что на элементе произведён долгий клик
     */
    private ItemLongClickListener longClickListener;

    /***
     * Создать адаптер управляющий выводом данных из списка на экран
     * @param sessions привязанный список сеансов
     */
    public SessionsAdapter(ArrayList<Session> sessions) {
        setNewList(sessions);
    }

    /***
     * Привязать список данных для последующего отображения на экране
     * @param sessions список сеансов
     */
    public void setNewList(ArrayList<Session> sessions) {
        data = sessions;
        notifyDataSetChanged();
    }

    /***
     * Создать виджет, в котором будут показаны данные о сеансе
     * @param parent контейнер всех отображаемых элементов RecyclerView
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        // используется форма отображения данных о сеансе
        View view = inflater.inflate(R.layout.a_session, parent, false);
        return new ViewHolder(view);
    }

    /***
     * Вывести на экран данные об одном сеансе.
     * @param holder виджет используемый в качестве элемента списка
     * @param position позиция данных в привязанном списке данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = getItem(position);
        holder.t_session_day.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(session.date));
        holder.t_session_time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(session.date));
        holder.t_session_cinema_name.setText(FileDatabase.findCinemaById(session.cinema_id).name);
        long duration = FileDatabase.findCinemaById(session.cinema_id).duration;
        holder.t_session_duration.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(duration));
        holder.t_session_price.setText(String.format(Locale.ENGLISH, "%.2f", session.price));
    }

    /***
     * @return общее количество данных в списке
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /***
     * Получить данные из привязанного списка по номеру позиции
     * @param position позиция в списке
     * @return данные о сеансе типа Session
     */
    public Session getItem(int position) {
        return data.get(position);
    }

    /***
     * Обработчик нажатия на элемент списка
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /***
     * Подключить обработчик короткого клика по элементу видимого списка
     * @param itemClickListener обработчик клика
     */
    void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    /***
     * Обработчик долгого нажатия на элемент списка
     */
    public interface ItemLongClickListener {
        void onItemClick(View view, int position);
    }

    /***
     * Подключить обработчик долгого клика по элементу видимого списка
     * @param itemLongClickListener обработчик клика
     */
    void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        longClickListener = itemLongClickListener;
    }


    /***
     * Класс отвечающий за связывание виджетов одного элемента списка.
     * В последующем в каждый из этих виджетов будут внесены данные
     * из привязанного списка.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private Spinner spin_hall_session;
        private TextView t_session_day;
        private TextView t_session_time;
        private TextView t_session_cinema_name;
        private TextView t_session_duration;
        private TextView t_session_price;


        private ViewHolder(View itemView) {
            super(itemView);

            t_session_day = itemView.findViewById(R.id.t_session_day);
            t_session_time = itemView.findViewById(R.id.t_session_time);
            t_session_cinema_name = itemView.findViewById(R.id.t_session_cinema_name);
            t_session_duration = itemView.findViewById(R.id.t_session_duration);
            t_session_price = itemView.findViewById(R.id.t_session_price);

            // привязываем слушатели нажатий к каждому элементу видимого списка

            itemView.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onItemClick(v, getAdapterPosition());
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null)
                    longClickListener.onItemClick(v, getAdapterPosition());
                return false;
            });
        }
    }

}
