package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity.SettingsActivity;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.service.RepositoryCheckService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.AlarmManagerUtils;

/**
 * Created by Dmitriy Korobeynikov on 1/12/2015.
 * Used to start RepositoryCheckService and setup alarm manager for sending pending intent.
 */
public class RepositoryBroadcastReceiver extends BroadcastReceiver {

    public static final String LOG_TAG = RepositoryBroadcastReceiver.class.getSimpleName();

    public static final String RECEIVER_ACTION = "com.johnkuper.epam.action.RepositoryBroadcastReceiver";
    public static final int CHECK_SERVICE_REQUEST_CODE = 154635;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive");

        long checkFrequencyInMinutes = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsActivity.PREF_CHECK_FREQUENCY_KEY, "0")) * DateUtils.MINUTE_IN_MILLIS;
        if (checkFrequencyInMinutes != 0) {
            startRepositoryCheckService(context);
            AlarmManagerUtils.setupAlarmManager(context, checkFrequencyInMinutes, getPendingIntent(context));
        }
    }

    private void startRepositoryCheckService(Context context) {
        Intent serviceIntent = new Intent(context, RepositoryCheckService.class);
        context.startService(serviceIntent);
    }

    public static PendingIntent getPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, CHECK_SERVICE_REQUEST_CODE, getIncomingIntent(), 0);
    }

    public static Intent getIncomingIntent() {
        Intent intent = new Intent();
        intent.setAction(RECEIVER_ACTION);
        return intent;
    }
}
