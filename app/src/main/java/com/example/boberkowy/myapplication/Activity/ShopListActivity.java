package com.example.boberkowy.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.boberkowy.myapplication.Adapter.ProductListAdapter;
import com.example.boberkowy.myapplication.Adapter.ShopListAdapter;
import com.example.boberkowy.myapplication.DAO.Shop;
import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends AppCompatActivity {

    private List<Shop> shopList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference permissionReference;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("shop");
        permissionReference = FirebaseDatabase.getInstance().getReference("permission");
        firebaseAuth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("TAG", "signInAnonymously: success");
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(ShopListActivity.this, "Zautoryzowano poprawnie, UID: " + user.getUid(), Toast.LENGTH_SHORT).show();
                        getShopList();
                    }else {
                        Log.d(TAG,"signInAnonymously: failed");
                        Toast.makeText(ShopListActivity.this, "Coś sie popsuło i nie można Cie było zautoryzować", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(ShopListActivity.this, "Zautoryzowano poprawnie, UID: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            getShopList();
        }
    }
    private void initRecyclerView() {
        RecyclerView rc = findViewById(R.id.shop_list_view);
        RecyclerView.Adapter adapter = new ShopListAdapter(shopList);
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getShopList() {
        databaseReference.addValueEventListener(valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                shopList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Shop shop = ds.getValue(Shop.class);
                    shopList.add(shop);
                    Log.e("Get Data", shop.getName());
                }
                Toast.makeText(ShopListActivity.this, "Masz uprawnienia  ", Toast.LENGTH_SHORT).show();
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }
    public void addShop(View view) {
        Intent intent = new Intent(this,AddShopActivity.class);
        intent.putExtra("shop_id","0");
        startActivity(intent);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void goToMapActivity(View view) {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
