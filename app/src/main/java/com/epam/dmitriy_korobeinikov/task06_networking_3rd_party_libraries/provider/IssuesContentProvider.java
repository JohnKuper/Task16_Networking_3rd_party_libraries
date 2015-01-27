package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.AUTHORITY;
import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.IssueContent;

/**
 * Created by Dmitriy Korobeynikov on 27.01.2015.
 */
public class IssuesContentProvider extends ContentProvider {

    public static final String LOG_TAG = IssuesContentProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, IssueContent.ISSUE_PATH, IssueContent.ISSUE_URI_PATTERN_MANY);
        URI_MATCHER.addURI(AUTHORITY, IssueContent.ISSUE_PATH + "/#", IssueContent.ISSUE_URI_PATTERN_ONE);
    }

    private SQLiteDatabase mSQLiteDatabase;
    private IssuesDBHelper mIssuesDBHelper;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mIssuesDBHelper = new IssuesDBHelper(mContext);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> query with URI - " + uri.toString());

        switch (URI_MATCHER.match(uri)) {
            case IssueContent.ISSUE_URI_PATTERN_MANY:
                return handleIssuesQuery(uri, projection, selection, selectionArgs, sortOrder);
            default:
                throw new IllegalStateException("URI is not supported: " + uri);
        }
    }

    private Cursor handleIssuesQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(IssueContent.TABLE_NAME);
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = IssueContent.TITLE + " COLLATE NOCASE";
        }
        mSQLiteDatabase = mIssuesDBHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(mSQLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(mContext.getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> getType");
        switch (URI_MATCHER.match(uri)) {
            case IssueContent.ISSUE_URI_PATTERN_ONE:
                return IssueContent.ISSUE_CONTENT_ITEM_TYPE;
            case IssueContent.ISSUE_URI_PATTERN_MANY:
                return IssueContent.ISSUE_CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        checkForExistingIssuesUriPatterns(uri, "insert");
        mSQLiteDatabase = mIssuesDBHelper.getWritableDatabase();
        long row = mSQLiteDatabase.insertOrThrow(IssueContent.TABLE_NAME, null, values);
        Uri newUri = ContentUris.withAppendedId(IssueContent.ISSUES_URI, row);
        mContext.getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        checkForIssuesUriPatternOne(uri, "update");

        String id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
            selection = IssueContent._ID + " = " + id;
        }
        int updateRows = 0;
        try {
            mSQLiteDatabase = mIssuesDBHelper.getWritableDatabase();
            updateRows = mSQLiteDatabase.updateWithOnConflict(IssueContent.TABLE_NAME, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_ROLLBACK);
            mContext.getContentResolver().notifyChange(uri, null);
        } catch (SQLiteConstraintException e) {
            Log.e(RepositoriesApplication.APP_NAME, LOG_TAG + "> SQLite constraint during update " + e);
        }
        return updateRows;
    }

    private void checkForExistingIssuesUriPatterns(Uri uri, String methodName) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> " + methodName + ": " + uri.toString());
        if (URI_MATCHER.match(uri) != IssueContent.ISSUE_URI_PATTERN_ONE && URI_MATCHER.match(uri) != IssueContent.ISSUE_URI_PATTERN_MANY)
            throw new IllegalArgumentException("Wrong URI for " + methodName + " operation: " + uri);
    }

    private void checkForIssuesUriPatternOne(Uri uri, String methodName) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> " + methodName + ": " + uri.toString());
        if (URI_MATCHER.match(uri) != IssueContent.ISSUE_URI_PATTERN_ONE) {
            throw new IllegalArgumentException("Wrong URI for " + methodName + " operation: " + uri);
        }
    }


}
