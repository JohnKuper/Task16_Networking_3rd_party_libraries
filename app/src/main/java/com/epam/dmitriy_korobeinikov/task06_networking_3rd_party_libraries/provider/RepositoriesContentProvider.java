package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.OwnerContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;

/**
 * Created by Dmitriy Korobeynikov on 12/25/2014.
 * GitHub's repositories content provider.
 */
public class RepositoriesContentProvider extends ContentProvider {

    public static final String TAG = "ContentProvider";
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(BaseContent.AUTHORITY, RepositoryContent.REPOSITORY_PATH, RepositoryContent.REPOSITORY_URI_PATTERN_MANY);
        URI_MATCHER.addURI(BaseContent.AUTHORITY, RepositoryContent.REPOSITORY_PATH + "/#", RepositoryContent.REPOSITORY_URI_PATTERN_ONE);
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, " + uri.toString());
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String tables = RepositoryContent.TABLE_NAME + " JOIN "
                + OwnerContent.TABLE_NAME + " ON " + RepositoryContent.OWNER_ID + " = " + OwnerContent.FULL_ID;

        switch (URI_MATCHER.match(uri)) {
            case RepositoryContent.REPOSITORY_URI_PATTERN_MANY:
                queryBuilder.setTables(tables);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = RepositoryContent.STARGAZERS_COUNT + " DESC";
                }
                break;
            case RepositoryContent.REPOSITORY_URI_PATTERN_ONE:
                queryBuilder.setTables(tables);
                queryBuilder.appendWhere(RepositoryContent.FULL_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException("URI is not supported: " + uri);
        }
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(mSQLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), RepositoryContent.REPOSITORIES_URI);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType, " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case RepositoryContent.REPOSITORY_URI_PATTERN_MANY:
                return RepositoryContent.REPOSITORY_CONTENT_TYPE;
            case RepositoryContent.REPOSITORY_URI_PATTERN_ONE:
                return RepositoryContent.REPOSITORY_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, BaseContent.DATABASE_NAME, null, BaseContent.DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}

