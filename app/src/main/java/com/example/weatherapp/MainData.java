package com.example.weatherapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Define table name
@Entity(tableName = "tableLocation")
public class MainData implements Serializable {
    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    //Create location column
    @ColumnInfo(name = "location")
    private String location;

    // Generate getter and setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
