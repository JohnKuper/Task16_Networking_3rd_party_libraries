package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Dmitriy Korobeynikov on 27.01.2015.
 */
public class IssuesDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String LOG_TAG = IssuesDBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "Issues.db";
    private static final int DATABASE_VERSION = 1;

    public IssuesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Issue.class);
        } catch (SQLException e) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Error during creating the database");
            throw new RuntimeException("Error during creating the database");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
