package com.example.boberkowy.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.boberkowy.myapplication.Adapter.ProductListAdapter;
import com.example.boberkowy.myapplication.DAO.ProductLab;
import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductListActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getProductList();
        initRecyclerView();
    }

    public void getProductList() {
        ProductLab productLab = ProductLab.get(this);
        productList = productLab.getProducts();
    }

    private void initRecyclerView() {
        RecyclerView rc = findViewById(R.id.product_list_view);
        RecyclerView.Adapter adapter = new ProductListAdapter(productList, this);
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addProduct(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        intent.putExtra("product_id", "0");
        startActivity(intent);
    }
}
