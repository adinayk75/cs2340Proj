package com.example.travelapplication.viewmodel;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {
    private final MutableLiveData<Boolean> navigateToMain = new MutableLiveData<>();
    public SplashViewModel() {

    }

    public LiveData<Boolean> getNavigateToMain() {
        return navigateToMain;
    }

    public void startSplashTimer() {
        new Handler().postDelayed(() -> navigateToMain.setValue(true), 3000);
    }
}
