package com.example.fetching3.viewmodel;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.fetching3.Details_Pojo;
import com.example.fetching3.repository.WeatherRepository;
import com.example.fetching3.util.Resource;
import com.example.fetching3.worker.WeatherWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.fetching3.util.Constants.SYNC_DATA_WORK_NAME;
import static com.example.fetching3.util.Constants.TAG_SYNC_DATA;

public class RemoteSyncViewModel extends AndroidViewModel {
    private static final String TAG = "RemoteSyncViewModel";
    private final WeatherRepository mRepository;
    private final WorkManager mWorkManager;
    // New instance variable for the WorkInfo
    private final LiveData<List<WorkInfo>> mSavedWorkInfo;

    public RemoteSyncViewModel(WeatherRepository mRepository) {
        super(mRepository.getApplication());
        this.mRepository = mRepository;
        mWorkManager = WorkManager.getInstance(mRepository.getApplication());
        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_SYNC_DATA);
    }

    public void fetchData() {
        Log.i(TAG, "starting work.....");

        // Create Network constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(WeatherWorker.class, 15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        mWorkManager.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );

    }

    public LiveData<List<Details_Pojo>> getWeatherData() {
        return mRepository.getWeatherData();
    }

    public LiveData<Resource<List<Details_Pojo>>> fetchWeatherData() {
        return mRepository.fetchWeatherData();
    }

    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }
}
