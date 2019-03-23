package com.arsa.reader.common;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class InitializeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
