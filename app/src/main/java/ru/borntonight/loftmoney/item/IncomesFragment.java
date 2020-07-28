package ru.borntonight.loftmoney.item;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.borntonight.loftmoney.AddItemActivity;
import ru.borntonight.loftmoney.LoftApp;
import ru.borntonight.loftmoney.R;
import ru.borntonight.loftmoney.remote.MoneyItem;
import ru.borntonight.loftmoney.remote.MoneyResponse;

public class IncomesFragment extends Fragment {

    private ItemAdapter itemAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
                intentAddItem.putExtra("type", "income");
                startActivityForResult(intentAddItem, 1);
            }
        });
        getIncomes();

        return view;
    }

    // Обновление элементов при добавлении
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getIncomes();
        }
    }

    private void getIncomes() {
        final List<Item> items = new ArrayList<>();

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getItems("income")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoneyResponse>() {
                    @Override
                    public void accept(MoneyResponse moneyResponse) throws Exception {
                        for (MoneyItem moneyItem : moneyResponse.getMoneyItemList()) {
                            items.add(Item.getInstance(moneyItem));
                        }
                        itemAdapter.setData(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        // убить запрос (цепочку)
        compositeDisposable.add(disposable);
    }
}
