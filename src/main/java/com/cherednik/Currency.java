package com.cherednik;

import java.util.List;

public class Currency {

    List<Currency> exchangeRate;

    String currency;

    private double saleRate;

    private double purchaseRate;

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                ", saleRate=" + saleRate +
                ", purchaseRate=" + purchaseRate +
                '}';
    }
}
