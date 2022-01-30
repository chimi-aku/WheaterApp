package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Console;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton manageLocationButton;
    private RelativeLayout rlHome;
    private RecyclerView rvWeatherForecast;
    private ProgressBar pbLoading;
    private TextView tvCityName, tvMainTemperature, tvCondition;
    private ImageView imWeatherIcon, ivMainActivityBackground;

    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;

    MyProperties _myProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make aplication full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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
//        pbLoading = findViewById(R.id.pbLoading);
        tvCityName = findViewById(R.id.tvCityName);
        tvMainTemperature = findViewById(R.id.tvMainTemperature);
        tvCondition = findViewById(R.id.tvCondition);
        imWeatherIcon = findViewById(R.id.imWeatherIcon);
        ivMainActivityBackground = findViewById(R.id.ivMainActivityBackground);

        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModelArrayList);
        rvWeatherForecast.setAdapter(weatherRVAdapter);

        // Get instance of singletone which contains locations
        _myProperties = MyProperties.getInstance();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(_myProperties.chosenLocation != null && !_myProperties.chosenLocation.getLocation().equals("")) {
            tvCityName.setText(_myProperties.chosenLocation.getLocation());
        }
        else {
            tvCityName.setText("No location is chosen");
        }

    }

    public void openManageLocationActivity() {
        Intent intent = new Intent(this, ManageLocation.class);
        startActivity(intent);
    }

    private void  getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/current.json?key=53ebad91dc4546bb928103433212812&q="+ cityName +"&days=1&aqi=no";



    }



}

