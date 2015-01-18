package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by Dmitriy Korobeynikov on 18.01.2015.
 * Used for setup and cancel alarm manager.
 */
public class AlarmManagerUtils {

    public static void setupAlarmManager(Context context, long minutesInMillis, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + minutesInMillis, pendingIntent);
    }

    public static void cancelAlarmManager(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }
}
