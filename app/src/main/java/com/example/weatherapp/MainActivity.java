package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton manageLocationButton;
    private RelativeLayout rlHome;
    private RecyclerView rvWeatherForecast;
    private ProgressBar pbLoading;
    private TextView tvCityName, tvMainTemperature, tvCondition;
    private ImageView ivWeatherIcon, ivMainActivityBackground;
    private ImageButton btnGetMyLocation;

    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;

    private String cityName;
    private String cityNameFromCurrLocation;

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
        pbLoading = findViewById(R.id.pbLoading);
        tvCityName = findViewById(R.id.tvCityName);
        tvMainTemperature = findViewById(R.id.tvMainTemperature);
        tvCondition = findViewById(R.id.tvCondition);
        ivWeatherIcon = findViewById(R.id.imWeatherIcon);
        ivMainActivityBackground = findViewById(R.id.ivMainActivityBackground);
        btnGetMyLocation = findViewById(R.id.btnGetMyLocation);

        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModelArrayList);
        rvWeatherForecast.setAdapter(weatherRVAdapter);

        // Get instance of singletone which contains location
        _myProperties = MyProperties.getInstance();


        // Get permissions
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        // Get current location
        FusedLocationProviderClient locationProviderClient =  LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(l -> {
            cityNameFromCurrLocation = getCityNameFromCords(l.getLongitude(), l.getLatitude());
        });


        // Get whether for current location
        btnGetMyLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tvCityName.setText(cityNameFromCurrLocation);
                getWeatherInfo(cityNameFromCurrLocation);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(_myProperties.chosenLocation != null && !_myProperties.chosenLocation.getLocation().equals("")) {
            cityName = _myProperties.chosenLocation.getLocation();
            //tvCityName.setText(cityName);
            //Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
            getWeatherInfo(cityName);

        }
        else {
            tvCityName.setText("No location is chosen");
        }

    }


    public void openManageLocationActivity() {
        Intent intent = new Intent(this, ManageLocation.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Please provide the permision", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityNameFromCords(double longitude, double latitude) {
        String cityName = "Not found";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 10);

            for (Address adr : addressList) {
                if(adr != null) {
                    String city = adr.getLocality();
                    if(city != null && !city.equals("")) {
                        cityName = city;
                    }
                    else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User City Not...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }

    private void  getWeatherInfo(String cityName){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=53ebad91dc4546bb928103433212812&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        //Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        tvCityName.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                pbLoading.setVisibility(View.GONE);
//                rlHome.setVisibility(View.VISIBLE);
                weatherRVModelArrayList.clear();

                try {
                    // Get all essential Data from JSON Object

                    // Get current weather
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    tvMainTemperature.setText(temperature + "°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");

                    // Condition
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(ivWeatherIcon);
                    tvCondition.setText(condition);

                    // Get forecast
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastDay = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hoursArray = forecastDay.getJSONArray("hour");

                    for(int i = 0; i < hoursArray.length(); i++) {
                        // Get Hours Data from JSON Object
                        JSONObject hourObj = hoursArray.getJSONObject(i);
                        String timeH = hourObj.getString("time");
                        String temperatureH = hourObj.getString("temp_c");
                        String conditionIconH = hourObj.getJSONObject("condition").getString("icon");
                        String windH = hourObj.getString("wind_kph");

                        weatherRVModelArrayList.add(new WeatherRVModel(timeH, temperatureH, conditionIconH, windH));
                    }

                    weatherRVAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }



}

