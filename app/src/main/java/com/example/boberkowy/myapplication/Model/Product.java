package com.example.boberkowy.myapplication.Model;

import java.util.UUID;

public class Product {

    private UUID mId;
    private String mName;
    private String mCount;
    private String mPrice;
    private boolean mPurchased;

    public Product(){
        mId = UUID.randomUUID();
    }

    public Product(UUID id){mId = id;}

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
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
