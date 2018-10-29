package com.example.boberkowy.myapplication.Model;

import java.util.UUID;

public class ProductsLists {

    public UUID id;
    public UUID productId;
    public UUID productListId;

    public ProductsLists(){this.id = UUID.randomUUID();}
    public ProductsLists(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductListId() {
        return productListId;
    }

    public void setProductListId(UUID productListId) {
        this.productListId = productListId;
    }
}
