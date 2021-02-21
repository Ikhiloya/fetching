package com.example.fetching3;

import android.content.Context;
import android.graphics.Movie;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import okhttp3.Response;

public class MyWorker extends Worker {

    //public static List<Movie> list;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //getData();
        return Result.success();
    }


//    public void getData() {
//        RetrofitBuilder.getData().getTopRatedMovies(Utils.apikey, Utils.language, 1)
//                .enqueue(new retrofit2.Call<List<Details_Pojo>> {
//                    @Override
//                    public void onResponse(Call<List<Details_Pojo>> call, Response<Details_Pojo> response) {
//                        if (response.isSuccessful()) {
//                            list = response.body().getMovies();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Details_Pojo> call, Throwable t) {
//                        Log.d("TODO", "Excep" + t.toString());
//                    }
//                });
//
//
//        @Override
//        public void onStopped () {
//            super.onStopped();
//            Log.i(TAG, "OnStopped called for this worker");
//        }
//    }
}
