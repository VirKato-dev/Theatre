package my.example.theatre;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ChairsAdapter extends RecyclerView.Adapter<ChairsAdapter.ViewHolder> {

    /***
     * Ссылка на привязанный список сеансов
     */
    private ArrayList<Boolean> data;

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
     * @param chairs привязанный список сеансов
     */
    public ChairsAdapter(ArrayList<Boolean> chairs) {
        setNewList(chairs);
    }

    /***
     * Привязать список данных для последующего отображения на экране
     * @param chairs список сеансов
     */
    public void setNewList(ArrayList<Boolean> chairs) {
        data = chairs;
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
        View view = inflater.inflate(R.layout.a_chair, parent, false);
        return new ViewHolder(view);
    }

    /***
     * Вывести на экран данные об одном сеансе.
     * @param holder виджет используемый в качестве элемента списка
     * @param position позиция данных в привязанном списке данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Boolean chair = getItem(position);
        int num = position % Hall.redHallCol;

        // номер места в ряду
        holder.t_chair.setText(String.format("%02d", num+1));
        // красным если занято
        if (chair) holder.i_chair.setColorFilter(Color.RED);
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
    public Boolean getItem(int position) {
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
        private FrameLayout l_chair;
        private ImageView i_chair;
        private TextView t_chair;


        private ViewHolder(View itemView) {
            super(itemView);

            l_chair = itemView.findViewById(R.id.l_chair);
            i_chair = itemView.findViewById(R.id.i_chair);
            t_chair = itemView.findViewById(R.id.t_chair);

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
