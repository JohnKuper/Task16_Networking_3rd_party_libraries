package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;

/**
 * Created by Dmitriy Korobeynikov on 1/13/2015.
 */
public class SettingsActivity extends PreferenceActivity {

    public static final String PREF_REPO_NAME_KEY = "prefRepoName";
    public static final String PREF_OWNER_LOGIN_KEY = "prefOwnerLogin";
    public static final String PREF_CHECK_FREQUENCY_KEY = "prefCheckFrequency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getPreferenceScreen().getSharedPreferences()
//                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        getPreferenceScreen().getSharedPreferences()
//                .unregisterOnSharedPreferenceChangeListener(this);
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        String keyValue = sharedPreferences.getString(key, "");
//        Preference checkFrequencyPref = findPreference(key);
//        if (key.equals(PREF_CHECK_FREQUENCY_KEY) && (!keyValue.equals("0"))) {
//            Log.d(BaseContent.LOG_TAG_TASK_06, "Selected value is: " + keyValue);
//            checkFrequencyPref.setSummary(sharedPreferences.getString(key, "") + " minute(s)");
//            sendBroadCastForStartCheckService();
//        } else if (key.equals(PREF_CHECK_FREQUENCY_KEY) && (keyValue.equals("0"))) {
//            Log.d(BaseContent.LOG_TAG_TASK_06, "Cancel value was selected");
//            checkFrequencyPref.setSummary("Never");
//            cancelAlarmManager();
//        }
//    }

    private void sendBroadCastForStartCheckService() {
        Intent intent = new Intent();
        intent.setAction(RepositoryBroadcastReceiver.RECEIVER_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(intent);
    }

    private void cancelAlarmManager() {
        Intent receiverIntent = new Intent();
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), RepositoryBroadcastReceiver.CHECK_SERVICE_REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmPendingIntent.cancel();
        alarmManager.cancel(alarmPendingIntent);
    }


}
