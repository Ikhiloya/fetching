package com.example.fetching3.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.fetching3.Api;
import com.example.fetching3.App;
import com.example.fetching3.Details_Pojo;
import com.example.fetching3.R;
import com.example.fetching3.db.dao.WeatherDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WeatherWorker extends Worker {
    private final Api weatherService;
    private final WeatherDao weatherDao;

    private static final String TAG = WeatherWorker.class.getSimpleName();

    public WeatherWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        weatherService = App.get().getBookService();
        weatherDao = App.get().getWeatherDao();
    }

    @NonNull
    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();
        //simulate slow work
        // WorkerUtils.makeStatusNotification("Fetching Data", applicationContext);
        Log.i(TAG, "Fetching Data from Remote host");
        WorkerUtils.sleep();

        try {
            //create a call to network
            Call<List<Details_Pojo>> call = weatherService.getstatus();
            Response<List<Details_Pojo>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {

//                String data = WorkerUtils.toJson(response.body());
                Log.i(TAG, "data fetched from network successfully");

                //delete existing book data
                weatherDao.deleteWeatherData();

                weatherDao.saveWeatherData(response.body());

//                WorkerUtils.makeStatusNotification(applicationContext.getString(R.string.new_data_available), applicationContext);

                return Result.success();
            } else {
                return Result.retry();
            }


        } catch (Throwable e) {
            e.printStackTrace();
            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error fetching data", e);
            return Result.failure();
        }
    }


    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }
}
