package com.example.boberkowy.myapplication.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.boberkowy.myapplication.Adapter.ProductListAdapter;
import com.example.boberkowy.myapplication.DAO.ProductLab;
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
import java.util.UUID;

public class ProductListActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference permissionReference;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "FIREBASE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
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
                        Toast.makeText(ProductListActivity.this, "Zautoryzowano poprawnie, UID: " + user.getUid(), Toast.LENGTH_SHORT).show();
                        getProductList();
                    }else {
                        Log.d(TAG,"signInAnonymously: failed");
                        Toast.makeText(ProductListActivity.this, "Coś sie popsuło i nie można Cie było zautoryzować", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(ProductListActivity.this, "Zautoryzowano poprawnie, UID: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            getProductList();
        }
    }

    public void getProductList() {
        databaseReference.addValueEventListener(valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                    productList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        productList.add(product);
                        Log.e("Get Data", product.getName());
                    }
                Toast.makeText(ProductListActivity.this, "Masz uprawnienia  ", Toast.LENGTH_SHORT).show();
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
//        ProductLab productLab = ProductLab.get(this);
//        productList = productLab.getProducts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("asd", "WSZEDŁEM DO DESTROYA W PRODUCT LIST");
        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }
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

    public void backToMain(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
