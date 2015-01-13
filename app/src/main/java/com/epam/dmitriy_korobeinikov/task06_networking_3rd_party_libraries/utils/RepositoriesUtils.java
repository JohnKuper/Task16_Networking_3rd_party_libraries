package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.BuildConfig;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;
import com.ocpsoft.pretty.time.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dmitriy Korobeynikov on 08.01.2015.
 * Converts date in human readable convenient format.
 */
public class RepositoriesUtils {

    public static String getElapsedDate(Cursor cursor, String columnName) {
        Date date = getFormatDate(cursor, columnName, "yyyy-MM-dd HH:mm:ss.S");
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(date);
    }

    public static Date getFormatDate(Cursor cursor, String columnName, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String stringDate = cursor.getString(cursor.getColumnIndex(columnName));
        Date date = null;

        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            Log.e(BuildConfig.APPLICATION_ID, "Error during date parsing: ", e);
        }
        return date;
    }

    public static String getFormatDateAsString(Cursor cursor, String columnName, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = getFormatDate(cursor, columnName, format);
        return formatter.format(date);
    }

    public static boolean isEditTextEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences;
    }

}
