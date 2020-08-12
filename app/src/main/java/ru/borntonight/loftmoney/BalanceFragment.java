package ru.borntonight.loftmoney;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.borntonight.loftmoney.remote.BalanceResponse;

public class BalanceFragment extends Fragment {

    private TextView balance, expense, income;
    private BalanceView balanceView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        balance = view.findViewById(R.id.txtBalanceFinanceValue);
        expense = view.findViewById(R.id.expense);
        income = view.findViewById(R.id.income);
        balanceView = view.findViewById(R.id.balanceView);
        updateDate();
    }

    public static BalanceFragment getInstance() {
        return new BalanceFragment();
    }

    private void updateDate() {
        String token = getActivity().getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().balance(token)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BalanceResponse>() {
                    @Override
                    public void accept(BalanceResponse balanceResponse) {
                        String balanceString = String.valueOf(balanceResponse.getIncome()-balanceResponse.getExpense());
                        balance.setText(balanceString + "₽");
                        income.setText(String.valueOf(balanceResponse.getIncome()) + "₽");
                        expense.setText(String.valueOf(balanceResponse.getExpense()) + "₽");
                        balanceView.update(balanceResponse.getExpense(), balanceResponse.getIncome());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                });
        compositeDisposable.add(disposable);
    }
}
