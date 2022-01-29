package com.example.weatherapp;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao {
    // Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    // Delete query
    @Delete
    void delete(MainData mainData);

    // Update query
    @Query("UPDATE tableLocation SET location = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    // Get all data query
    @Query("SELECT * FROM tableLocation")
    List<MainData> getAll();

}
