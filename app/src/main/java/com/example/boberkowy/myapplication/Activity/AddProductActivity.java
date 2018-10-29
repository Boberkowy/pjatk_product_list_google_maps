package com.example.boberkowy.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boberkowy.myapplication.DAO.ProductLab;
import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.Model.ProductList;
import com.example.boberkowy.myapplication.R;

import java.util.UUID;

public class AddProductActivity extends AppCompatActivity{

    private Product mProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        handleExtra();
        createProductNameField();
        createProductPriceField();
        createProductCountField();
    }

    public void handleExtra(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("product_id");
        Button addButton = findViewById(R.id.add_product);
        Button editButton = findViewById(R.id.edit_product);
        Button deleteButton = findViewById(R.id.delete_button);
        if(id.equals("0")){
            mProduct = new Product();
            addButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }else{
            mProduct = ProductLab.get(this).getProduct(UUID.fromString(id));
            addButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    public void addProduct(View view) {
        ProductLab productLab = ProductLab.get(this);
        productLab.addProduct(mProduct);

        Toast.makeText(this,"Dodano pomyślnie",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,ProductListActivity.class));
    }

    public void editProduct(View view){
        ProductLab productLab = ProductLab.get(this);
        productLab.updateProduct(mProduct);

        Toast.makeText(this,"Edytowano pomyślnie",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,ProductListActivity.class));

    }

    private void createProductNameField() {

        EditText mProductField = findViewById(R.id.product_name);
        mProductField.setText(mProduct.getName());
        mProductField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProduct.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }

    private void createProductPriceField() {

        EditText mProductPrice = findViewById(R.id.price);
        mProductPrice.setText(mProduct.getPrice());
        mProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProduct.setPrice(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }

    private void createProductCountField() {

        EditText mProductCount = findViewById(R.id.count);
        mProductCount.setText(mProduct.getCount());
        mProductCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProduct.setCount(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }


    public void deleteProduct(View view) {
        ProductLab.get(this).deleteProduct(mProduct.getId());
        Toast.makeText(this,"Usunięto pomyślnie",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, ProductListActivity.class));
    }
}
