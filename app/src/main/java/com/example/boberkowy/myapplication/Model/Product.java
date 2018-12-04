package com.example.boberkowy.myapplication.Model;

import java.util.UUID;

public class Product {

    private String mId;
    private String mName;
    private String mCount;
    private String mPrice;
    private boolean mPurchased;

    public Product(){
    }

    public Product(String id){mId = id;}

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String mCount) {
        this.mCount = mCount;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public boolean isPurchased() {
        return mPurchased;
    }

    public void setPurchased(boolean mPurchased) {
        this.mPurchased = mPurchased;
    }
}
