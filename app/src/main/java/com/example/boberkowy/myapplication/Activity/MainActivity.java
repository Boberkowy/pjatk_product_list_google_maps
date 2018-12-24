package com.example.boberkowy.myapplication.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.*;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_FONT_SIZE;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_NAME;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FirebaseAuth as ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        prepareSharedPreferences();
        getButtonsPreferences();
    }

    public void addButtonHandler(View view) {
        Intent intent = new Intent(this,ProductListActivity.class);
        startActivity(intent);
    }

    public void settingsButtonHandler(View view) {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    private void getButtonsPreferences(){
        Button settingsButton = findViewById(R.id.settings_button);
        Button productButton = findViewById(R.id.add_button);
        String color =  sharedPreferences.getString(PREFERENCES_FONT_COLOR, "black");
        settingsButton.setTextSize(sharedPreferences.getFloat(PREFERENCES_FONT_SIZE, 16f));
        productButton.setTextColor(Color.parseColor(color));
    }

    @SuppressLint("ResourceType")
    private void prepareSharedPreferences(){
        if(sharedPreferences.getString(PREFERENCES_NAME,PREFERENCES_FONT_COLOR).isEmpty()){
            sharedPreferences.edit().putString(PREFERENCES_FONT_COLOR,getResources().getString(R.color.black)).apply();
        }
    }


    public void shopListButton(View view) {
        Intent intent = new Intent(this,ShopListActivity.class);
        startActivity(intent);
    }

    public void testBroadcast(View view) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        intent.setAction("com.example.boberkowy.myapplication");
//        intent.putExtra("product_id","mop");
//        Log.d("BROAD", "Sending broadcast");
//        sendBroadcast(intent,"com.example.mypermission");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        final DatabaseReference product = database.getReference("product");
        final Product pro = new Product();
        pro.setName("firebase");
        pro.setPurchased(true);
        pro.setCount("123");
        pro.setPrice("123");

        product.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                product.child(pro.getName()).setValue(pro);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.setValue("Hello, World!");
    }

}
