package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.service.RepositoryCheckService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesUtils;

/**
 * Created by Dmitriy Korobeynikov on 1/12/2015.
 */
public class RepositoryBroadcastReceiver extends BroadcastReceiver {

    public static final String RECEIVER_ACTION = "com.johnkuper.epam.action.RepositoryCheckService";
    public static final int CHECK_SERVICE_REQUEST_CODE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(BaseContent.LOG_TAG_TASK_06, "onReceive invoke");

        int checkFrequency = Integer.parseInt(RepositoriesUtils.getSharedPreferences(context).getString("prefCheckFrequency", "-1"));
        if (checkFrequency != 0 && checkFrequency != -1) {
            startRepositoryCheckService(context);
            setupAlarmManagerForSendPendingIntent(context, checkFrequency);
        }
    }

    private void startRepositoryCheckService(Context context) {
        Intent serviceIntent = new Intent(context, RepositoryCheckService.class);
        context.startService(serviceIntent);
    }

    private void setupAlarmManagerForSendPendingIntent(Context context, int checkFrequency) {
        Intent receiverIntent = new Intent();
        receiverIntent.setAction(RECEIVER_ACTION);
        receiverIntent.addCategory(Intent.CATEGORY_DEFAULT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, CHECK_SERVICE_REQUEST_CODE, receiverIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int checkFrequencyInMinutes = checkFrequency * 1000 * 60;
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + checkFrequencyInMinutes, pendingIntent);
    }
}
