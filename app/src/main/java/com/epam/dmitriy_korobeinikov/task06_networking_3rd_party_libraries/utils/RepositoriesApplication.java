package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dmitriy Korobeynikov on 19.01.2015.
 * Helps to get application context from any place.
 */

public class RepositoriesApplication extends Application {

    private static Context mContext;
    public static final String APP_NAME = "RepositoriesSearch";
    public static final String GITHUB_API_URL = "https://api.github.com";

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
