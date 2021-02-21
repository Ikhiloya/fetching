package com.example.fetching3.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fetching3.Details_Pojo;
import com.example.fetching3.db.dao.WeatherDao;

@Database(entities = {Details_Pojo.class}, version = 1, exportSchema = false)
public abstract class WeatherDatabase  extends RoomDatabase {
    private static volatile WeatherDatabase INSTANCE;

    public abstract WeatherDao bookDao();

    public static WeatherDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherDatabase.class, "weather_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
