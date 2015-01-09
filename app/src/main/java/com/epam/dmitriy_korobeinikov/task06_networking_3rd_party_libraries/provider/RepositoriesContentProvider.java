package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.OwnerContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Korobeynikov on 12/25/2014.
 * GitHub's repositories content provider.
 */
public class RepositoriesContentProvider extends ContentProvider {

    public static final String TAG = "ContentProvider";
    private static final UriMatcher URI_MATCHER;
    private static final Map<String,String> mProjectionMap;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(BaseContent.AUTHORITY, RepositoryContent.REPOSITORY_PATH, RepositoryContent.REPOSITORY_URI_PATTERN_MANY);
        URI_MATCHER.addURI(BaseContent.AUTHORITY, RepositoryContent.REPOSITORY_PATH + "/#", RepositoryContent.REPOSITORY_URI_PATTERN_ONE);
        mProjectionMap = buildProjectionMap();
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        return true;
    }

    private static Map<String, String> buildProjectionMap() {
        Map<String, String> projectionMap = new HashMap<>();

        projectionMap.put(RepositoryContent._ID, RepositoryContent.FULL_ID);
        projectionMap.put(RepositoryContent.FULL_ID, RepositoryContent.FULL_ID + " AS repository_id");
        projectionMap.put(RepositoryContent.NAME, RepositoryContent.NAME);
        projectionMap.put(RepositoryContent.FULL_NAME, RepositoryContent.FULL_NAME);
        projectionMap.put(RepositoryContent.DESCRIPTION, RepositoryContent.DESCRIPTION);
        projectionMap.put(RepositoryContent.CREATED_AT, RepositoryContent.CREATED_AT);
        projectionMap.put(RepositoryContent.UPDATED_AT, RepositoryContent.UPDATED_AT);
        projectionMap.put(RepositoryContent.STARGAZERS_COUNT, RepositoryContent.STARGAZERS_COUNT);
        projectionMap.put(RepositoryContent.LANGUAGE, RepositoryContent.LANGUAGE);
        projectionMap.put(RepositoryContent.TAGS_ID, RepositoryContent.TAGS_ID);

        projectionMap.put(OwnerContent.LOGIN, OwnerContent.LOGIN);
        projectionMap.put(OwnerContent.AVATAR_URL, OwnerContent.AVATAR_URL);
        projectionMap.put(OwnerContent.TYPE, OwnerContent.TYPE);

        return projectionMap;
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
                queryBuilder.setProjectionMap(mProjectionMap);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = RepositoryContent.STARGAZERS_COUNT + " DESC";
                }
                break;
            case RepositoryContent.REPOSITORY_URI_PATTERN_ONE:
                queryBuilder.setTables(tables);
                queryBuilder.setProjectionMap(mProjectionMap);
                queryBuilder.appendWhere(RepositoryContent.FULL_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException("URI is not supported: " + uri);
        }
        Cursor cursor = queryBuilder.query(mSQLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), RepositoryContent.REPOSITORIES_URI);
        return cursor;    }

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
        long row = mSQLiteDatabase.insert(TagContent.TABLE_NAME, "", values);
        // If record is added successfully
        Uri newUri = ContentUris.withAppendedId(RepositoryContent.REPOSITORIES_URI, row);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
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

