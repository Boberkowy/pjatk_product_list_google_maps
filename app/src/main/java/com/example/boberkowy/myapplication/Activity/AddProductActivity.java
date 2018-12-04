package com.example.boberkowy.myapplication.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boberkowy.myapplication.DAO.ProductLab;
import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private Product mProduct = new Product();
    private DatabaseReference productDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        handleExtra();
        createProductNameField();
        createProductPriceField();
        createProductCountField();
    }

    public void handleExtra() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("product_id");
        Button addButton = findViewById(R.id.add_product);
        Button editButton = findViewById(R.id.edit_product);
        Button deleteButton = findViewById(R.id.delete_button);

        if (id.equals("0")) {
            productDatabase = FirebaseDatabase.getInstance().getReference("product");
            addButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        } else {
            productDatabase = FirebaseDatabase.getInstance().getReference("product").child(id);
//            mProduct.setId(intent.getStringExtra("product_id"));
            mProduct.setName(intent.getStringExtra("product_name"));
            mProduct.setPrice(intent.getStringExtra("product_price"));
            mProduct.setCount(intent.getStringExtra("product_count"));
            mProduct.setPurchased(intent.getBooleanExtra("product_purchased", false));
            addButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    public void addProduct(View view) {
//        final ProductLab productLab = ProductLab.get(this);
//        productLab.addProduct(mProduct);

        String id = productDatabase.push().getKey();
        mProduct.setId(id);
        productDatabase.child(id).setValue(mProduct);

        Toast.makeText(this, "Dodano pomyślnie", Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.example.boberkowy.myapplication");
        intent.putExtra("product_id", mProduct.getName());
        Log.d("BROAD", "Sending broadcast");
        sendBroadcast(intent, "com.example.mypermission");

        startActivity(new Intent(this, ProductListActivity.class));
    }

    public void editProduct(View view) {
//        ProductLab productLab = ProductLab.get(this);
//        productLab.updateProduct(mProduct);

        productDatabase.child("name").setValue(mProduct.getName());
        productDatabase.child("count").setValue(mProduct.getCount());
        productDatabase.child("price").setValue(mProduct.getPrice());
        productDatabase.child("purchased").setValue(mProduct.isPurchased());

        Toast.makeText(AddProductActivity.this, "Edytowano pomyślnie: " + mProduct.getName(), Toast.LENGTH_LONG).show();
        startActivity(new Intent(AddProductActivity.this, ProductListActivity.class));

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
//        ProductLab.get(this).deleteProduct(mProduct.getId());

        productDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Usunięto pomyślnie", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddProductActivity.this, "Usuwanie nie powiodło się", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(AddProductActivity.this, ProductListActivity.class));
            }
        });
    }
}
