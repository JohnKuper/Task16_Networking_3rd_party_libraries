package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Dmitriy_Korobeinikov on 12/25/2014.
 */
public class RepositoriesContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.johnkuper.epam.githubcontentprovider";
    public static final String TAG = "ContentProvider";

    public static class Owner implements BaseColumns {

        public static final String TABLE_NAME = "owners";
        public static final String OWNER_PATH = TABLE_NAME;

        // Content uri pattern code
        public static final int OWNER_URI_PATTERN_MANY = 1;
        public static final int OWNER_URI_PATTERN_ONE = 2;

        //Table fields
        public static final String OWNER_ID = "owner_id";
        public static final String LOGIN = "login";
        public static final String AVATAR_URL = "avatar_url";
        public static final String TYPE = "type";
        public static final String SITE_ADMIN = "site_admin";
        public static final String REPOSITORY_ID = "repository_id";

        //Owner Uri
        public static final Uri OWNERS_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(OWNER_PATH)
                .build();

        //Data types
        public static final String OWNER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + OWNER_PATH;
        public static final String OWNER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + OWNER_PATH;
    }

    public static class Repository implements BaseColumns {

        public static final String TABLE_NAME = "repositories";
        public static final String REPOSITORY_PATH = TABLE_NAME;

        // Content uri pattern code
        public static final int REPOSITORY_URI_PATTERN_MANY = 3;
        public static final int REPOSITORY_URI_PATTERN_ONE = 4;

        public static final String REPO_ID = "repo_id";
        public static final String NAME = "name";
        public static final String FULL_NAME = "full_name";
        public static final String PRIVATE = "private";
        public static final String DESCRIPTION = "description";
        public static final String CREATED_AT = "created_at";
        public static final String STARGAZERS_COUNT = "stargazers_count";
        public static final String OWNER_ID = "owner_id";

        public static final Uri REPOSITORIES_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(REPOSITORY_PATH)
                .build();

        //Data types
        public static final String REPOSITORY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + REPOSITORY_PATH;
        public static final String REPOSITORY_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + REPOSITORY_PATH;
    }

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY, Owner.OWNER_PATH, Owner.OWNER_URI_PATTERN_MANY);
        URI_MATCHER.addURI(AUTHORITY, Owner.OWNER_PATH + "/#", Owner.OWNER_URI_PATTERN_ONE);

        URI_MATCHER.addURI(AUTHORITY, Repository.REPOSITORY_PATH, Repository.REPOSITORY_URI_PATTERN_MANY);
        URI_MATCHER.addURI(AUTHORITY, Repository.REPOSITORY_PATH + "/#", Repository.REPOSITORY_URI_PATTERN_ONE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case Owner.OWNER_URI_PATTERN_MANY:
                Log.d(TAG, "query with uri OWNER_URI_PATTERN_MANY");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = Owner.LOGIN + " ASC";
                }
                break;
            case Owner.OWNER_URI_PATTERN_ONE:
                String id = uri.getLastPathSegment();
                Log.d(TAG, "query with uri OWNER_URI_PATTERN_ONE " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = Owner.OWNER_ID + "=" + id;
                } else {
                    selection = selection + " AND " + Owner.OWNER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalStateException("URI is not supported: " + uri);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
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
}
