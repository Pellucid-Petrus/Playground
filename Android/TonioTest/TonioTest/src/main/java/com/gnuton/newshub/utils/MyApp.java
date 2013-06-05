package com.gnuton.newshub.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by gnuton on 6/5/13.
 */

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
