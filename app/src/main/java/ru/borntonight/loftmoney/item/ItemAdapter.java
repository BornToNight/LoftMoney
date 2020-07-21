package ru.borntonight.loftmoney.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import ru.borntonight.loftmoney.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private ItemAdapterClick itemAdapterClick;

    public void setItemAdapterClick(ItemAdapterClick itemAdapterClick) {
        this.itemAdapterClick = itemAdapterClick;
    }

    public void setData(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addData(List<Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    // какой View использовать для каждого элемента
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ItemViewHolder(itemAdapterClick, LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    // запись данных в View
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView priceView;
        private ItemAdapterClick itemAdapterClick;

        public ItemViewHolder(ItemAdapterClick itemAdapterClick, @NonNull View itemView) {
            super(itemView);
            this.itemAdapterClick = itemAdapterClick;
            // ищем именно ВНУТРИ view item
            nameView = itemView.findViewById(R.id.itemNameView);
            priceView = itemView.findViewById(R.id.itemPriceView);
        }

        // параметр - откуда берём данные
        public void bind(final Item item) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (itemAdapterClick != null) {
                        itemAdapterClick.onItemClick(item);
                    }
                }
            });

            // устанавливаем данные в TextView, которые получили при bind
            nameView.setText(item.getName());
            priceView.setText(item.getPrice());
            priceView.setTextColor(ContextCompat.getColor(priceView.getContext(), item.getColor()));
        }
    }
}
