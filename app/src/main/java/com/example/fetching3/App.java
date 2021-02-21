package com.example.fetching3;

import android.app.Application;

import com.example.fetching3.db.WeatherDatabase;
import com.example.fetching3.db.dao.WeatherDao;
import com.example.fetching3.factory.LiveDataCallAdapterFactory;
import com.example.fetching3.util.AppExecutors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static final String BASE_URL = "https://run.mocky.io/v3/4db28732-a4dc-482a-bbf8-0dfad8884d78/";
    private Api weatherService;

    private static App INSTANCE;

    private static AppExecutors mAppExecutors;

    private static WeatherDao weatherDao;


    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        //Gson Builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
//        Timber.plant(new Timber.DebugTree());

        // HttpLoggingInterceptor
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
//                .addInterceptor(httpLoggingInterceptor)
                .build();


        //Retrofit
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //BookService
        weatherService = mRetrofit.create(Api.class);
        mAppExecutors = new AppExecutors();
        weatherDao = WeatherDatabase.getDatabase(getApplicationContext()).bookDao();


    }

    public Api getBookService() {
        return weatherService;
    }


    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public AppExecutors getExecutors() {
        return mAppExecutors;
    }

}
