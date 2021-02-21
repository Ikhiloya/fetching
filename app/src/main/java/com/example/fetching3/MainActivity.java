package com.example.fetching3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.WorkInfo;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import com.example.fetching3.factory.ViewModelFactory;
import com.example.fetching3.repository.WeatherRepository;
import com.example.fetching3.util.AppExecutors;
import com.example.fetching3.util.Resource;
import com.example.fetching3.util.Status;
import com.example.fetching3.viewmodel.RemoteSyncViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RemoteSyncViewModel mRemoteSyncViewModel;

    TextView nametxt, agetxt, phonetxt;
    Button retrieveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewModel
        Api weatherService = App.get().getBookService();
        WeatherRepository mRepository = new WeatherRepository(getApplication(), weatherService, new AppExecutors());
        ViewModelFactory factory = new ViewModelFactory(mRepository);
        mRemoteSyncViewModel = ViewModelProviders.of(this, factory).get(RemoteSyncViewModel.class);

        mRemoteSyncViewModel.fetchData();

        nametxt = (TextView) findViewById(R.id.nametxt);
        agetxt = (TextView) findViewById(R.id.agetxt);
        phonetxt = (TextView) findViewById(R.id.phonetxt);
        retrieveBtn = (Button) findViewById(R.id.retrieveBtn);

        retrieveBtn.setOnClickListener(this);


        // Show work info, goes inside onCreate()
        mRemoteSyncViewModel.getOutputWorkInfo().observe(this, listOfWorkInfo -> {

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_SYNC_DATA
            WorkInfo workInfo = listOfWorkInfo.get(0);
            Log.i(TAG, "WorkState: " + workInfo.getState());
            if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
//                showWorkFinished();

                //observe Room db
                mRemoteSyncViewModel.getWeatherData().observe(this, data -> {
                    if (data == null || data.isEmpty()) {
                        return;
                    }
                    Details_Pojo adslist = data.get(0);
                    String name = adslist.getName();
                    String age = adslist.getAge();
                    String phone = adslist.getPhone();

                    nametxt.setText(name);
                    agetxt.setText(age);
                    phonetxt.setText(phone);

                });


            } else {
//                showWorkInProgress();
            }
        });



    }


/*
    private void fetchData() {

        ///
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class,
//                15, TimeUnit.MINUTES)
//                .setConstraints(constraints)
//                .build();
//        androidx.work.WorkManager.getInstance().enqueueUniquePeriodicWork("tags",
//                ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);

        ///

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Details_Pojo>> call = api.getstatus();
        call.enqueue(new Callback<List<Details_Pojo>>() {
            @Override
            public void onResponse(Call<List<Details_Pojo>> call, Response<List<Details_Pojo>> response) {
                List<Details_Pojo> adslist = response.body();

                String name = adslist.get(0).getName();
                String age = adslist.get(0).getAge();
                String phone = adslist.get(0).getPhone();

                nametxt.setText(name);
                agetxt.setText(age);
                phonetxt.setText(phone);
            }
            @Override
            public void onFailure(Call<List<Details_Pojo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retrieveBtn) {
            fetchData();
        }
    }

    private void fetchData() {
        mRemoteSyncViewModel.fetchWeatherData().observe(this, new Observer<Resource<List<Details_Pojo>>>() {
            @Override
            public void onChanged(Resource<List<Details_Pojo>> resource) {
                if (null != resource) {
                    if (resource.status.equals(Status.LOADING)) {
//                        dialogUtil.showProgressDialog(getResources().getString(R.string.loading_loan_requests_msg));
                    }
                    if (resource.status.equals(Status.SUCCESS) && null != resource.data && !resource.data.isEmpty()) {
//                        dialogUtil.hideProgressDialog();
                        Details_Pojo adslist = resource.data.get(0);
                        String name = adslist.getName();
                        String age = adslist.getAge();
                        String phone = adslist.getPhone();

                        nametxt.setText(name);
                        agetxt.setText(age);
                        phonetxt.setText(phone);
                    } else if (resource.status.equals(Status.ERROR)) {
//                        dialogUtil.hideProgressDialog();
//                        DialogUtil.showDialog(context, getResources().getString(R.string.error), getResources().getString(R.string.error_msg));

                    }
                } else {
//                    dialogUtil.hideProgressDialog();
//                    DialogUtil.showDialog(context, getResources().getString(R.string.error), getResources().getString(R.string.error_msg));
                }
            }
        });

    }
}