package my.example.theatre;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CinemasAdapter extends RecyclerView.Adapter<CinemasAdapter.ViewHolder> {

    /***
     * Ссылка на привязанный список фильмов
     */
    private ArrayList<Cinema> data;

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
     * @param cinemas привязанный список фильмов
     */
    public CinemasAdapter(ArrayList<Cinema> cinemas) {
        setNewList(cinemas);
    }

    /***
     * Привязать список данных для последующего отображения на экране
     * @param cinemas список фильмов
     */
    public void setNewList(ArrayList<Cinema> cinemas) {
        data = cinemas;
        notifyDataSetChanged();
    }

    /***
     * Создать виджет, в котором будут показаны данные о фильме
     * @param parent контейнер всех отображаемых элементов RecyclerView
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        // используется форма отображения данных о фильме
        View view = inflater.inflate(R.layout.a_cinema, parent, false);
        return new ViewHolder(view);
    }

    /***
     * Вывести на экран данные об одном фильме.
     * @param holder виджет используемый в качестве элемента списка
     * @param position позиция данных в привязанном списке данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cinema cinema = getItem(position);
        // вывод данных о фильме
        holder.t_cinema_name.setText(cinema.name);
        holder.t_cinema_duration.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(cinema.duration));
        holder.t_cinema_description.setText(cinema.description);
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
     * @return данные о фильме типа Cinema
     */
    public Cinema getItem(int position) {
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
        private TextView t_cinema_name;
        private TextView t_cinema_duration;
        private TextView t_cinema_description;


        private ViewHolder(View itemView) {
            super(itemView);

            t_cinema_name = itemView.findViewById(R.id.t_cinema_name);
            t_cinema_duration = itemView.findViewById(R.id.t_cinema_duration);
            t_cinema_description = itemView.findViewById(R.id.t_cinema_description);

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
