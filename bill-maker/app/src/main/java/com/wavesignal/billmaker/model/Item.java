package com.wavesignal.billmaker.model;

import android.util.Log;

public class Item {
    private static final String TAG = "BillMaker";
    private String name;
    private String rate;
    private String rateUnit;
    private String qty;
    private String qtyUnit;
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateUnit() {
        return rateUnit;
    }

    public void setRateUnit(String rateUnit) {
        this.rateUnit = rateUnit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void calculateAndSaveAmount() {
        try {
            int rate = Integer.parseInt(this.getRate());
            int qty = Integer.parseInt(this.getQty());

            String rateUnit = this.getRateUnit();
            String qtyUnit = this.getQtyUnit();

            setAmount(String.valueOf(rate * qty));
        }catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }
    @Override
    public String toString() {
        return "name:" + name + "\nrate: " + rate +
                "\nrateUnit: " + rateUnit + "\nDate: " + qty + "\nBillNo: " + qty;
    }
}
