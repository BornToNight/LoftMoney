package ru.borntonight.loftmoney.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.borntonight.loftmoney.AddItemActivity;
import ru.borntonight.loftmoney.MainActivity;
import ru.borntonight.loftmoney.R;

public class BudgetFragment extends Fragment {

    private ItemAdapter itemAdapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        RecyclerView recyclerView = view.findViewById(R.id.priceRecycleView);
        itemAdapter = new ItemAdapter();

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        itemAdapter.setItemAdapterClick(new ItemAdapterClick() {
            @Override
            public void onItemClick(Item item) {
                // todo действие при нажатии
            }
        });

        FloatingActionButton buttonAdd = view.findViewById(R.id.floatingActionButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddItem = new Intent(getActivity(), AddItemActivity.class);
                startActivityForResult(intentAddItem, 1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            // fixme цвет в зависимости от доход/расход
            Item item = new Item(data.getStringExtra("name"), data.getStringExtra("price"), R.color.colorAppleGreen );
            itemAdapter.addData(item);
        }
    }
}
