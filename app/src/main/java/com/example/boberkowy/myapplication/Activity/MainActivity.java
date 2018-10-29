package com.example.boberkowy.myapplication.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.boberkowy.myapplication.Helpers.StaticsVariables;
import com.example.boberkowy.myapplication.R;

import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.*;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_FONT_SIZE;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_NAME;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

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
}
