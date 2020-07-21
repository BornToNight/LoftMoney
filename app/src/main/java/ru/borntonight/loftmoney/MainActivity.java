package ru.borntonight.loftmoney;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.borntonight.loftmoney.item.Item;
import ru.borntonight.loftmoney.item.ItemAdapter;
import ru.borntonight.loftmoney.item.ItemAdapterClick;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.priceRecycleView);
        itemAdapter = new ItemAdapter();

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        itemAdapter.addData(generateExpenses());
        itemAdapter.addData(generateIncomes());
        itemAdapter.setItemAdapterClick(new ItemAdapterClick() {
            @Override
            public void onItemClick(Item item) {
                // todo действие при нажатии
            }
        });
    }

    private List<Item> generateExpenses() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Молоко", "50 ₽", R.color.colorExpense));
        return items;
    }

    private List<Item> generateIncomes() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Премия", "7500 ₽", R.color.colorAppleGreen));
        return items;
    }
}