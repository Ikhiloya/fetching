package com.example.fetching3.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.fetching3.Api;
import com.example.fetching3.Details_Pojo;
import com.example.fetching3.db.WeatherDatabase;
import com.example.fetching3.db.dao.WeatherDao;
import com.example.fetching3.util.ApiResponse;
import com.example.fetching3.util.AppExecutors;
import com.example.fetching3.util.NetworkBoundResource;
import com.example.fetching3.util.Resource;

import java.util.List;

public class WeatherRepository {

    private static WeatherDao weatherDao;
    private final Api weatherService;
    private final AppExecutors appExecutors;
    private final android.app.Application application;
    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();


    public WeatherRepository(android.app.Application application, Api weatherService,
                             AppExecutors appExecutors) {
        this.application = application;
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        weatherDao = db.bookDao();
        this.weatherService = weatherService; //TODO:
        this.appExecutors = appExecutors;
    }


    public LiveData<List<Details_Pojo>> getWeatherData() {
        return weatherDao.getWeather();
    }

    public LiveData<Resource<List<Details_Pojo>>> fetchWeatherData() {
        /**
         * List<Details_Pojo> is the [ResultType]
         * List<Details_Pojo> is the [RequestType]
         */
        return new NetworkBoundResource<List<Details_Pojo>, List<Details_Pojo>>(appExecutors) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void saveCallResult(@NonNull List<Details_Pojo> weatherData) {
                Log.d(LOG_TAG, "call to delete book in db");
                weatherDao.deleteWeatherData();
                Log.d(LOG_TAG, "call to insert results to db");
                weatherDao.saveWeatherData(weatherData);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected boolean shouldFetch(@Nullable List<Details_Pojo> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Details_Pojo>> loadFromDb() {
                Log.d(LOG_TAG, " call to load from db");
                return weatherDao.getWeather();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Details_Pojo>>> createCall() {
                Log.d(LOG_TAG, "creating a call to network");
                return weatherService.getWeatherData();
            }

            @Override
            protected List<Details_Pojo> processResponse(ApiResponse<List<Details_Pojo>> response) {
                return super.processResponse(response);
            }
        }.asLiveData();
    }


    public android.app.Application getApplication() {
        return application;
    }
}
