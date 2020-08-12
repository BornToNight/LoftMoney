package ru.borntonight.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    @SerializedName("total_expenses") private long expense;
    @SerializedName("total_income") private long income;

    public long getExpense() {
        return expense;
    }

    public long getIncome() {
        return income;
    }
}
