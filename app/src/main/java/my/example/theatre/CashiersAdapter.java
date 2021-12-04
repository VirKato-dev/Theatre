package my.example.theatre;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CashiersAdapter extends RecyclerView.Adapter<CashiersAdapter.ViewHolder> {

    /***
     * Ссылка на привязанный список кассиров
     */
    private ArrayList<Cashier> data;

    /***
     * Инструмент позволяющий сделать из XML-файла виджет для последующего вывода на экран
     */
    private LayoutInflater inflater;

    /***
     * Интерфейс для передачи сигнала о том, что элемент видимого списка был нажат
     */
    private ItemClickListener clickListener;

    /***
     * Интерфейс для передачи сигнала о том, что на элементе произведён долгий клик
     */
    private ItemLongClickListener longClickListener;

    /***
     * Создать адаптер управляющий выводом данных из списка на экран
     * @param cashiers привязанный список кассиров
     */
    public CashiersAdapter(ArrayList<Cashier> cashiers) {
        setNewList(cashiers);
    }

    /***
     * Привязать список данных для последующего отображения на экране
     * @param cashiers список кассиров
     */
    public void setNewList(ArrayList<Cashier> cashiers) {
        data = cashiers;
        notifyDataSetChanged();
    }

    /***
     * Создать виджет, в котором будут показаны данные о кассире
     * @param parent контейнер всех отображаемых элементов RecyclerView
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        // используется форма отображения данных о кассире
        View view = inflater.inflate(R.layout.a_cashier, parent, false);
        return new ViewHolder(view);
    }

    /***
     * Вывести на экран данные об одном кассире.
     * @param holder виджет используемый в качестве элемента списка
     * @param position позиция данных в привязанном списке данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cashier cashier = getItem(position);
        // вывод данных о кассире
        holder.t_cashier_name.setText(cashier.name);
        holder.t_cashier_login.setText(cashier.login);
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
     * @return данные о кассире типа User
     */
    public Cashier getItem(int position) {
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
     * В последующем в каждый из этих виджжетов будут внесены данные
     * из привязанного списка.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView t_cashier_name;
        private TextView t_cashier_login;


        private ViewHolder(View itemView) {
            super(itemView);

            t_cashier_name = itemView.findViewById(R.id.t_cashier_name);
            t_cashier_login = itemView.findViewById(R.id.t_cashier_login);

            // привязываем слушатели нажатий к каждому элементу видимого списка

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) clickListener.onItemClick(v, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null)
                        longClickListener.onItemClick(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }

}
