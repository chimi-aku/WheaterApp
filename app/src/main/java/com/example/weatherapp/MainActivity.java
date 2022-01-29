package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button manageLocationButton;

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
    }

    public void openManageLocationActivity() {
        Intent intent = new Intent(this, ManageLocation.class);
        startActivity(intent);
    }

}