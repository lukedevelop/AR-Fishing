package com.example.arfishing;

public class BaitItem {

    int baitId;
    String baitName;
    String baitExplain;
    int baitPrice;
    int hasBaitAmount;

    BaitItem(String baitName, String baitExplain, int baitPrice, int hasBaitAmount, int baitId) {

        this.baitName = baitName;
        this.baitExplain = baitExplain;
        this.baitPrice = baitPrice;
        this.hasBaitAmount = hasBaitAmount;
        this.baitId = baitId;
    }

    public int getBaitId() {
        return baitId;
    }

    public void setBaitId(int baitId) {
        this.baitId = baitId;
    }

    public String getBaitName() {
        return baitName;
    }

    public void setBaitName(String baitName) {
        this.baitName = baitName;
    }

    public String getBaitExplain() {
        return baitExplain;
    }

    public void setBaitExplain(String baitExplain) {
        this.baitExplain = baitExplain;
    }

    public int getBaitPrice() {
        return baitPrice;
    }

    public void setBaitPrice(int baitPrice) {
        this.baitPrice = baitPrice;
    }

    public int getHasBaitAmount() {
        return hasBaitAmount;
    }

    public void setHasBaitAmount(int hasBaitAmount) {
        this.hasBaitAmount = hasBaitAmount;
    }
}


