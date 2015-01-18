package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.database.Cursor;
import android.util.Log;

import com.ocpsoft.pretty.time.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dmitriy Korobeynikov on 08.01.2015.
 * Converts date in human readable convenient format.
 */
public class RepositoriesDateUtils {

    public static final String LOG_TAG = RepositoriesDateUtils.class.getSimpleName();

    public static String getElapsedDate(Cursor cursor, String columnName) {
        Date date = getFormatDate(cursor, columnName, "yyyy-MM-dd HH:mm:ss.S");
        PrettyTime prettyTime = new PrettyTime(getAppLocale());
        return prettyTime.format(date);
    }

    public static Date getFormatDate(Cursor cursor, String columnName, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, getAppLocale());
        String stringDate = cursor.getString(cursor.getColumnIndex(columnName));
        Date date = null;

        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error during date parsing: ", e);
        }
        return date;
    }

    public static String getFormatDateAsString(Cursor cursor, String columnName, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, getAppLocale());
        Date date = getFormatDate(cursor, columnName, format);
        return formatter.format(date);
    }

    public static Locale getAppLocale() {
        return RepositoriesApplication.getAppContext().getResources().getConfiguration().locale;
    }
}
