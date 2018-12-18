package com.example.boberkowy.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boberkowy.myapplication.DAO.Shop;
import com.example.boberkowy.myapplication.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShopActivity extends AppCompatActivity {

    private Shop shop = new Shop();
    private DatabaseReference productDatabase;
    private DatabaseReference permissionReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        handleExtra();
        createShopNameField();
        createShopDescField();
        createShopLatiitudeField();
        createShopLongitudeField();
        createShopRadiusField();
        permissionReference = FirebaseDatabase.getInstance().getReference("permission");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void deleteShop(View view) {
        productDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddShopActivity.this, "Usunięto pomyślnie", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddShopActivity.this, "Usuwanie nie powiodło się", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(AddShopActivity.this, ShopListActivity.class));
            }
        });
    }

    public void editShop(View view) {
        if(firebaseAuth.getCurrentUser() != null){
            productDatabase.child("name").setValue(shop.getName());
            productDatabase.child("desc").setValue(shop.getDescription());
            productDatabase.child("radius").setValue(shop.getRadius());
            productDatabase.child("longitude").setValue(shop.getLongitude());
            productDatabase.child("latitude").setValue(shop.getLatitude());

            Toast.makeText(AddShopActivity.this, "Edytowano pomyślnie: " + shop.getName(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddShopActivity.this, ShopListActivity.class));
        }else{
            Toast.makeText(AddShopActivity.this, "Nie masz uprawnień", Toast.LENGTH_LONG).show();
        }
    }

    public void addShop(View view) {

        if(firebaseAuth.getCurrentUser() != null){

            String id = productDatabase.push().getKey();
            shop.setId(id);
            productDatabase.child(id).setValue(shop);

            Toast.makeText(this, "Dodano pomyślnie", Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setAction("com.example.boberkowy.myapplication");
            intent.putExtra("product_id", shop.getName());
            Log.d("BROAD", "Sending broadcast");
            sendBroadcast(intent, "com.example.mypermission");

            startActivity(new Intent(this, ShopListActivity.class));
        }else{
            Toast.makeText(this, "Brak uprawnień", Toast.LENGTH_SHORT).show();
        }
    }

    private void createShopNameField(){

        EditText mShopField = findViewById(R.id.shop_name);
        mShopField.setText(shop.getName());
        mShopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shop.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }


    private void createShopDescField(){

        EditText mShopField = findViewById(R.id.desc);
        mShopField.setText(shop.getDescription());

        mShopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shop.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }

    private void createShopRadiusField(){

        EditText mShopField = findViewById(R.id.radius);
        mShopField.setText(shop.getRadius());
        mShopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shop.setRadius(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }


    private void createShopLongitudeField(){

        EditText mShopField = findViewById(R.id.longitude);
        mShopField.setText(shop.getLongitude());
        mShopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shop.setLongitude(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }


    private void createShopLatiitudeField(){

        EditText mShopField = findViewById(R.id.latitude);
        mShopField.setText(shop.getLatitude());
        mShopField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shop.setLatitude(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing
            }
        });
    }


    public void handleExtra() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("shop_id");
        LatLng latLng = intent.getParcelableExtra("latlng");

        Button addButton = findViewById(R.id.add_shop);
        Button editButton = findViewById(R.id.edit_shop);
        Button deleteButton = findViewById(R.id.delete_button);

        if (id.equals("0")) {
            productDatabase = FirebaseDatabase.getInstance().getReference("shop");
            shop.setLatitude(String.valueOf(latLng.latitude));
            shop.setLongitude(String.valueOf(latLng.longitude));

            addButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        } else {
            productDatabase = FirebaseDatabase.getInstance().getReference("shop").child(id);
//            mProduct.setId(intent.getStringExtra("product_id"));
            shop.setName(intent.getStringExtra("shop_name"));
            shop.setDescription(intent.getStringExtra("shop_desc"));
            shop.setRadius(intent.getStringExtra("shop_radius"));
            shop.setLatitude(intent.getStringExtra("shop_latitude"));
            shop.setLongitude(intent.getStringExtra("shop_longitude"));
            addButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
}
