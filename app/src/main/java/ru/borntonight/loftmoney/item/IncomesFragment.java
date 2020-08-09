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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.borntonight.loftmoney.LoftApp;
import ru.borntonight.loftmoney.R;
import ru.borntonight.loftmoney.remote.MoneyItem;

public class IncomesFragment extends Fragment {

    private ItemAdapter itemAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SwipeRefreshLayout swipeRefreshLayout;

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

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getIncomes();
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
        String token = this.getActivity().getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getItems(token, "income")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        for (MoneyItem moneyItem : moneyItems) {
                            items.add(Item.getInstance(moneyItem));
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        itemAdapter.setData(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

        // убить запрос (цепочку)
        compositeDisposable.add(disposable);
    }
}
