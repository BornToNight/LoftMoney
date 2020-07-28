package ru.borntonight.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyResponse {
    @SerializedName("status") String status;
    @SerializedName("data") List<MoneyItem> moneyItemList;

    public String getStatus() {
        return status;
    }

    public List<MoneyItem> getMoneyItemList() {
        return moneyItemList;
    }
}