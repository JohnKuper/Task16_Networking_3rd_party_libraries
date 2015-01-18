package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.*;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.ViewsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Korobeynikov on 12/25/2014.
 * GitHub's repositories content provider.
 */
public class RepositoriesContentProvider extends ContentProvider {

    public static final String LOG_TAG = RepositoriesContentProvider.class.getSimpleName();
    private static final String ONLY_UNIQUE_TAGS = "The repository can only have unique tags";

    private static final UriMatcher URI_MATCHER;
    private static final Map<String, String> mProjectionMap;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        //Repository's uri
        URI_MATCHER.addURI(RepositoriesContract.AUTHORITY, RepositoryContent.REPOSITORY_PATH, RepositoriesContract.RepositoryContent.REPOSITORY_URI_PATTERN_MANY);
        URI_MATCHER.addURI(RepositoriesContract.AUTHORITY, RepositoryContent.REPOSITORY_PATH + "/#", RepositoryContent.REPOSITORY_URI_PATTERN_ONE);

        //Tag's uri
        URI_MATCHER.addURI(RepositoriesContract.AUTHORITY, TagContent.TAG_PATH, TagContent.TAG_URI_PATTERN_MANY);
        URI_MATCHER.addURI(RepositoriesContract.AUTHORITY, TagContent.TAG_PATH + "/#", TagContent.TAG_URI_PATTERN_ONE);

        mProjectionMap = buildProjectionMap();
    }

    private SQLiteDatabase mSQLiteDatabase;
    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
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

        projectionMap.put(OwnerContent.LOGIN, OwnerContent.LOGIN);
        projectionMap.put(OwnerContent.AVATAR_URL, OwnerContent.AVATAR_URL);
        projectionMap.put(OwnerContent.TYPE, OwnerContent.TYPE);

        return projectionMap;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, ": query, " + uri.toString());
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
            case TagContent.TAG_URI_PATTERN_MANY:
                queryBuilder.setTables(TagContent.TABLE_NAME);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = TagContent.REPOSITORY_TAG + " COLLATE NOCASE";
                }
                break;
            default:
                throw new IllegalStateException("URI is not supported: " + uri);
        }
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(mSQLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, ": getType, " + uri.toString());
        switch (URI_MATCHER.match(uri)) {

            case RepositoryContent.REPOSITORY_URI_PATTERN_MANY:
                return RepositoryContent.REPOSITORY_CONTENT_TYPE;
            case RepositoryContent.REPOSITORY_URI_PATTERN_ONE:
                return RepositoryContent.REPOSITORY_CONTENT_ITEM_TYPE;

            case TagContent.TAG_URI_PATTERN_MANY:
                return TagContent.TAG_CONTENT_TYPE;
            case TagContent.TAG_URI_PATTERN_ONE:
                return TagContent.TAG_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        checkPatternOneUriForTagContent(uri, "insert");

        long row = 0;
        try {
            mSQLiteDatabase = mDBHelper.getWritableDatabase();
            row = mSQLiteDatabase.insertOrThrow(TagContent.TABLE_NAME, null, values);
        } catch (SQLiteConstraintException e) {
            Log.e(LOG_TAG, ": SQLite constraint during insert ", e);
            ViewsUtils.showToast(getContext(), ONLY_UNIQUE_TAGS, Toast.LENGTH_LONG);
        }
        Uri newUri = ContentUris.withAppendedId(TagContent.TAGS_URI, row);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        checkPatternOneUriForTagContent(uri, "delete");

        String id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
            selection = TagContent.REPOSITORY_ID + " = " + id;
        } else {
            selection = selection + " AND " + TagContent.REPOSITORY_ID + " = " + id;
        }
        mSQLiteDatabase = mDBHelper.getWritableDatabase();
        int deleteRows = mSQLiteDatabase.delete(TagContent.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return deleteRows;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        checkPatternOneUriForTagContent(uri, "update");

        String id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
            throw new IllegalArgumentException("repository_tag WHERE clause should be specified");
        } else {
            selection = selection + " AND " + TagContent.REPOSITORY_ID + " = " + id;
        }
        int updateRows;
        try {
            mSQLiteDatabase = mDBHelper.getWritableDatabase();
            updateRows = mSQLiteDatabase.updateWithOnConflict(TagContent.TABLE_NAME, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_ROLLBACK);
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (SQLiteConstraintException e) {
            Log.e(LOG_TAG, ": SQLite constraint during update " + e);
            ViewsUtils.showToast(getContext(), ONLY_UNIQUE_TAGS, Toast.LENGTH_LONG);
            return -1;
        }

        return updateRows;
    }

    private void checkPatternOneUriForTagContent(Uri uri, String methodName) {
        Log.d(LOG_TAG, methodName + ": " + uri.toString());
        if (URI_MATCHER.match(uri) != TagContent.TAG_URI_PATTERN_ONE)
            throw new IllegalArgumentException("Wrong URI: " + uri);
    }

}

