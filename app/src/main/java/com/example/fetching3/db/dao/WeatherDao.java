package com.example.fetching3.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fetching3.Details_Pojo;

import java.util.List;

@Dao
public interface WeatherDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveWeather(Details_Pojo data);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWeatherData(List<Details_Pojo> data);

    @Transaction
    @Query("SELECT * FROM details_pojo WHERE id = :weatherId")
    LiveData<Details_Pojo> getBook(int weatherId);


    @Transaction
    @Query("SELECT * from details_pojo")
    LiveData<List<Details_Pojo>> getWeather();

    //delete
    @Transaction
    @Query("DELETE FROM details_pojo")
    void deleteWeatherData();
}
