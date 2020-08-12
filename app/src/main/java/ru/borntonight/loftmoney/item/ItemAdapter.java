package ru.borntonight.loftmoney.item;

import android.util.SparseBooleanArray;
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

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        selectedItems.put(position, !selectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        selectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < items.size(); i++) {
            if (selectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (Item item : items) {
            if (selectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

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

    public void addData(Item item) {
        items.add(item);
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
        holder.bind(items.get(position), selectedItems.get(position));
        holder.setListener(itemAdapterClick, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView priceView;

        public ItemViewHolder(ItemAdapterClick itemAdapterClick, @NonNull View itemView) {
            super(itemView);
            // ищем именно ВНУТРИ view item
            nameView = itemView.findViewById(R.id.itemNameView);
            priceView = itemView.findViewById(R.id.itemPriceView);
        }

        // параметр - откуда берём данные
        public void bind(final Item item, final boolean isSelected) {
            itemView.setSelected(isSelected);
            // устанавливаем данные в TextView, которые получили при bind
            nameView.setText(item.getName());
            priceView.setText(item.getPrice());
            priceView.setTextColor(ContextCompat.getColor(priceView.getContext(), item.getColor()));
        }

        public void setListener(final ItemAdapterClick listener, final Item item, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });
        }
    }
}
