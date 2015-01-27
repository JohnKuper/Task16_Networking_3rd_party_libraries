package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.SettingInjectorService;
import android.preference.PreferenceManager;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity.SettingsActivity;

/**
 * Created by Dmitriy Korobeynikov on 27.01.2015.
 */
public class PreferencesUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getCurrentOwnerLogin(Context context) {
        return getSharedPreferences(context).getString(SettingsActivity.PREF_OWNER_LOGIN_KEY, "");
    }

    public static String getCurrentRepoName(Context context) {
        return getSharedPreferences(context).getString(SettingsActivity.PREF_REPO_NAME_KEY, "");
    }
}
