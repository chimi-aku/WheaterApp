package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ManageLocation extends AppCompatActivity {

    EditText newLocation;
    Button btnAdd;
    RecyclerView locationsList;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_location);

        newLocation = findViewById(R.id.editTextLocation);
        btnAdd = findViewById(R.id.btnAddLocation);
        locationsList = findViewById(R.id.rvLocationsList);

        // Initialize database
        database = RoomDB.getInstance(this);
        // Store database value in data list
        dataList = database.mainDao().getAll();

        // Initialize linear
        linearLayoutManager = new LinearLayoutManager(this);
        // Set layout manager
        locationsList.setLayoutManager(linearLayoutManager);
        // Initalize adapter
        adapter = new MainAdapter(ManageLocation.this, dataList);
        // Set adapter
        locationsList.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get String from newLocation
                String sText = newLocation.getText().toString().trim();

                if(!sText.equals("")) {
                    MainData data = new MainData();
                    data.setLocation(sText);
                    database.mainDao().insert(data);
                    newLocation.setText("");
                    // Notify when data is inserted
                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
}