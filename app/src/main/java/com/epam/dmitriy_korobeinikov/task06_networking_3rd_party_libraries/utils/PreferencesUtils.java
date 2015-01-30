package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity.SettingsActivity;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;

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

    public static void setCurrentAccountName(Context context, String accountName) {
        getSharedPreferences(context).edit().putString(SettingsActivity.PREF_CURRENT_ACCOUNT_NAME, accountName).commit();
    }

    public static String getCurrentAccountName(Context context) {
        return getSharedPreferences(context).getString(SettingsActivity.PREF_CURRENT_ACCOUNT_NAME, "");
    }

    public static void eraseCurrentAccountName(Context context) {
        getSharedPreferences(context).edit().putString(SettingsActivity.PREF_CURRENT_ACCOUNT_NAME, "").commit();
    }

    public static void updateRepositoryCurrentStargazersCount(Context context, Repository repository) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(repository.getName(), repository.getStargazersCount());
        editor.apply();
    }
}
