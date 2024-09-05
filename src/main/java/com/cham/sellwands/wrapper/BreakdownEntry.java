package com.cham.sellwands.wrapper;

public class BreakdownEntry {

    private int amount;
    private double value;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addValue(double value) {
        this.value += value;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

}
