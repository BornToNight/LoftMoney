package ru.borntonight.loftmoney.item;

import ru.borntonight.loftmoney.R;
import ru.borntonight.loftmoney.remote.MoneyItem;

public class Item {

    private int id;
    private String name;
    private String price;
    private Integer color;

    public Item(int id, String name, String price, Integer color) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.color = color;
    }

    public static Item getInstance(MoneyItem moneyItem) {
        return new Item(moneyItem.getId(), moneyItem.getName(),
                moneyItem.getPrice() + "₽",
                moneyItem.getType().equals("expense") ? R.color.colorExpense : R.color.colorAppleGreen);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Integer getColor() {
        return color;
    }
}
