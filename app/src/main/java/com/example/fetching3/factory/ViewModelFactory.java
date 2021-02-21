package com.example.fetching3.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fetching3.repository.WeatherRepository;
import com.example.fetching3.viewmodel.RemoteSyncViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final WeatherRepository mRepository;

    public ViewModelFactory(WeatherRepository mRepository) {
        this.mRepository = mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(RemoteSyncViewModel.class))
            return (T) new RemoteSyncViewModel(mRepository);
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
