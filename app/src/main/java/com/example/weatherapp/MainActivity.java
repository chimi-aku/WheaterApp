package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageButton manageLocationButton;
    private RelativeLayout rlHome, rvWeatherForecast;
    private ProgressBar pbLoading;
    private TextView tvCityName, tvMainTemperature, tvCondition;
    private ImageView imWeatherIcon, ivMainActivityBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Go to manege Location
        manageLocationButton = findViewById(R.id.ManageLocationButton);
        manageLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openManageLocationActivity();
            }
        });

        // Assign other elements
        rlHome = findViewById(R.id.rlHome);
        rvWeatherForecast = findViewById(R.id.rvWeatherForecast);
        pbLoading = findViewById(R.id.pbLoading);
        tvCityName = findViewById(R.id.tvCityName);
        tvCityName = findViewById(R.id.tvCityName);
        tvMainTemperature = findViewById(R.id.tvMainTemperature);
        tvCondition = findViewById(R.id.tvCondition);
        imWeatherIcon = findViewById(R.id.imWeatherIcon);
        ivMainActivityBackground = findViewById(R.id.ivMainActivityBackground);






    }

    public void openManageLocationActivity() {
        Intent intent = new Intent(this, ManageLocation.class);
        startActivity(intent);
    }

}