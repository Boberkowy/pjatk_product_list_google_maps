package com.example.boberkowy.myapplication.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.boberkowy.myapplication.DAO.Shop;
import com.example.boberkowy.myapplication.Helpers.GeofenceReceiver;
import com.example.boberkowy.myapplication.Helpers.GeofenceTransitionsIntentService;
import com.example.boberkowy.myapplication.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {
    private final String TAG = "FIREBASE";

    private GoogleMap mMap;
    private List<Shop> shopList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth;
    List<Geofence> geofences = new ArrayList<>();
    private GeofencingClient mGeofencingClient;

    private static final int RADIUS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("shop");
        firebaseAuth = FirebaseAuth.getInstance();

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInAnonymously: success");
                    } else {
                        Log.d(TAG, "signInAnonymously: failed");
                    }
                }
            });
        }
    }

    private void getShops() {
        databaseReference.addListenerForSingleValueEvent(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                shopList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Shop shop = ds.getValue(Shop.class);
                    shopList.add(shop);
                    Log.e("Get Data", shop.getName());
                }
                databaseReference.removeEventListener(valueEventListener);
                prepareMarkers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(MapsActivity.this, AddShopActivity.class);
                intent.putExtra("latlng", latLng);
                intent.putExtra("shop_id", "0");
                startActivity(intent);
            }
        });

        getShops();
    }


    @SuppressLint("MissingPermission")
    private void prepareMarkers() {
        if (shopList.size() > 0) {
            for (int i = 0; i < shopList.size(); i++) {
                Shop shop = shopList.get(i);
                LatLng marker = new LatLng(Double.parseDouble(shop.getLatitude()), Double.parseDouble(shop.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(marker).title(shop.getName()));
                buildGeofence(shop);
            }
            mGeofencingClient.addGeofences(buildGeofencingRequest(), geofencePendingIntent()).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MapsActivity.this, "Dodałem geofence ", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private GeofencingRequest buildGeofencingRequest() {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(this.geofences)
                .build();
    }

    private void buildGeofence(Shop shop) {
        this.geofences.add(new Geofence.Builder()
                .setRequestId(shop.getId())
                .setCircularRegion(Double.valueOf(shop.getLatitude()), Double.valueOf(shop.getLongitude()), Float.valueOf(shop.getRadius()))
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());

        mMap.addCircle(new CircleOptions().center(new LatLng(Double.valueOf(shop.getLatitude()),Double.valueOf(shop.getLongitude())))
        .strokeColor(Color.argb(50,70,70,70))
        .fillColor(Color.argb(100,150,150,150))
        .radius(Double.valueOf(shop.getRadius())));
    }

    private PendingIntent geofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceReceiver.class);
        return PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

}
