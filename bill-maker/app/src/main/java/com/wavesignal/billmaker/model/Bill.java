package com.wavesignal.billmaker.model;

import java.util.List;

public class Bill {
    private String title;
    private String custInfo;
    private String billDate;
    private String billNo;
    private List<Item> itemList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustInfo() {
        return custInfo;
    }

    public void setCustInfo(String custInfo) {
        this.custInfo = custInfo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void removeItem(Item item) {
        itemList.remove(item);
    }

    @Override
    public String toString() {
        return "Title:" + title + "\nCustomer details: " + custInfo + "\nBill date: " + billDate + "\nBill No: " + billNo;
    }
}
