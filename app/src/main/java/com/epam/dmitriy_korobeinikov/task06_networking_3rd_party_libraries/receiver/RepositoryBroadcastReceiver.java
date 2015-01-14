package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity.SettingsActivity;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.service.RepositoryCheckService;

/**
 * Created by Dmitriy Korobeynikov on 1/12/2015.
 */
public class RepositoryBroadcastReceiver extends BroadcastReceiver {

    public static final String RECEIVER_ACTION = "com.johnkuper.epam.action.RepositoryCheckService";
    public static final int CHECK_SERVICE_REQUEST_CODE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(BaseContent.LOG_TAG_TASK_06, "onReceive invoke");

        int checkFrequency = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsActivity.PREF_CHECK_FREQUENCY_KEY, "0"));
        if (checkFrequency != 0) {
            startRepositoryCheckService(context);
            setupAlarmManager(context, checkFrequency);
        }
    }

    private void startRepositoryCheckService(Context context) {
        Intent serviceIntent = new Intent(context, RepositoryCheckService.class);
        context.startService(serviceIntent);
    }

    private void setupAlarmManager(Context context, int checkFrequency) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int checkFrequencyInMinutes = checkFrequency * 1000 * 20;
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + checkFrequencyInMinutes, getPendingIntent(context));
    }

    public static void cancelAlarmManager(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(RepositoryBroadcastReceiver.RECEIVER_ACTION);
        return PendingIntent.getBroadcast(context, CHECK_SERVICE_REQUEST_CODE, intent, 0);
    }

}
