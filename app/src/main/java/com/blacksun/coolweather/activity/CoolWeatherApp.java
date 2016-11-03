package com.blacksun.coolweather.activity;

import android.app.Application;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/11/3 0003.
 */

public class CoolWeatherApp extends Application {

    public static CoolWeatherApp sApp;
    public static Gson sGson;

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance();
        getGson();
    }

    private static CoolWeatherApp getInstance() {
        if (sApp == null) {
            sApp = new CoolWeatherApp();
        }
        return sApp;
    }

    private static Gson getGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }
}
