package com.example.boberkowy.myapplication.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.boberkowy.myapplication.R;

import static android.widget.AdapterView.OnItemSelectedListener;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_FONT_COLOR;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_FONT_SIZE;
import static com.example.boberkowy.myapplication.Helpers.StaticsVariables.PREFERENCES_NAME;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    String fontColor = "";
    float fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        prepareFontColorsSpinner();
        prepareFontSizeSpinner();
    }

    public void saveSettingsButton(View view) {
        sharedPreferences.edit().putString(PREFERENCES_FONT_COLOR, fontColor).apply();
        sharedPreferences.edit().putFloat(PREFERENCES_FONT_SIZE, fontSize).apply();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void prepareFontColorsSpinner() {
        Spinner spinner = findViewById(R.id.font_color_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.font_colors_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        fontColor = getResources().getString(R.color.black);
                        break;
                    }
                    case 1: {
                        fontColor = getResources().getString(R.color.red);
                        break;
                    }
                    case 2: {
                        fontColor = getResources().getString(R.color.blue);
                        break;
                    }
                    case 3: {
                        fontColor = getResources().getString(R.color.green);
                        break;
                    }
                    case 4: {
                        fontColor = getResources().getString(R.color.pink);
                        break;
                    }
                    default:{
                        fontColor = "Czarny";
                    }
                }
            }

            @SuppressLint("ResourceType")
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fontColor = getResources().getString(R.color.black);
            }
        });
    }

    private void prepareFontSizeSpinner(){
        Spinner spinner = findViewById(R.id.font_size_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.font_size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:{
                        fontSize = Float.valueOf(getResources().getStringArray(R.array.font_size_array)[0]);
                        break;
                    }
                    case 1:{
                        fontSize = Float.valueOf(getResources().getStringArray(R.array.font_size_array)[1]);
                        break;
                    }
                    case 2:{
                        fontSize = Float.valueOf(getResources().getStringArray(R.array.font_size_array)[2]);
                        break;
                    }
                    default:{
                        fontSize = 8f;
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}
