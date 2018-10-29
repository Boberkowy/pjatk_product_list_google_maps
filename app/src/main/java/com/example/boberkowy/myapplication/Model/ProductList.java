package com.example.boberkowy.myapplication.Model;

import java.util.List;
import java.util.UUID;

public class ProductList {

    private UUID id;
    private String name;

    public ProductList(){this.id = UUID.randomUUID();}
    public ProductList(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
