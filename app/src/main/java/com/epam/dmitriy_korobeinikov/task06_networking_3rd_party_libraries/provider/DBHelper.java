package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Dmitriy_Korobeinikov on 12/24/2014.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    public DBHelper(Context context) {
        super(context, MyContract.DATABASE_NAME, null, MyContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
