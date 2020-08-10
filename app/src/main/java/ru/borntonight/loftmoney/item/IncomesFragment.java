package ru.borntonight.loftmoney.item;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import ru.borntonight.loftmoney.remote.AuthApi;
import ru.borntonight.loftmoney.remote.AuthResponse;
import ru.borntonight.loftmoney.remote.MoneyItem;

public class IncomesFragment extends Fragment implements ItemAdapterClick, ActionMode.Callback {

    private ItemAdapter itemAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;


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
        itemAdapter.setItemAdapterClick(this);


        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

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

    @Override
    public void onItemClick(final Item item, final int position) {
        itemAdapter.clearItem(position);
        if (actionMode != null) {
            actionMode.setTitle(getString(R.string.selected, String.valueOf(itemAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(final Item item, final int position) {
        if (actionMode == null) {
            getActivity().startActionMode(this);
        }
        itemAdapter.toggleItem(position);
        if (actionMode != null) {
            actionMode.setTitle(getString(R.string.selected, String.valueOf(itemAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
        this.actionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeItems();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        List<Integer> selectedItems = itemAdapter.getSelectedItemIds();
        String token = this.getActivity().getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
        for (Integer itemId : selectedItems) {

            Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().removeItem(String.valueOf(itemId.intValue()), token)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<AuthResponse>() {
                        @Override
                        public void accept(AuthResponse authResponse) {
                            getIncomes();
                            itemAdapter.clearSelections();
                            actionMode.setTitle(getString(R.string.selected, String.valueOf(itemAdapter.getSelectedSize())));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                        }
                    });
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        itemAdapter.clearSelections();
    }
}
