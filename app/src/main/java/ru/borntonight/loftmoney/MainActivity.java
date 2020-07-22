package ru.borntonight.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            // fixme цвет в зависимости от доход/расход
            Item item = new Item(data.getStringExtra("name"), data.getStringExtra("price"), R.color.colorAppleGreen );
            itemAdapter.addData(item);
        }
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

    public void addItemClick(View v) {
        Intent intentAddItem = new Intent(this, AddItemActivity.class);
        startActivityForResult(intentAddItem, 1);
    }
}