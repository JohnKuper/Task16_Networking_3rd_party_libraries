package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * Created by Dmitriy_Korobeinikov on 12/24/2014.
 */
public class MyContract {

    public static final String DATABASE_NAME = "Repositories";
    public static final int DATABASE_VERSION = 1;

    public static final String AUTHORITY = "com.johnkuper.epam.ormlitecontentprovider";

    // content uri pattern code
    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;

    public static final String MIMETYPE_NAME = AUTHORITY + ".provider";

    public static final Uri BASE_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();

    public static class Owner implements BaseColumns {

        public static final String TABLE_NAME = "owners";
        public static final String OWNER_URI_PATH = TABLE_NAME;

        public static final String MIMETYPE_TYPE = TABLE_NAME;

        public static final String ID = "id";
        public static final String LOGIN = "login";
        public static final String AVATAR_URL = "avatar_url";
        public static final String TYPE = "type";
        public static final String SITE_ADMIN = "site_admin";

        public static final Uri OWNERS_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(OWNER_URI_PATH)
                .build();
    }

    public static class Repository implements BaseColumns {

        public static final String TABLE_NAME = "repositories";
        public static final String REPOSITORY_URI_PATH = TABLE_NAME;

        public static final String MIMETYPE_TYPE = TABLE_NAME;

        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String FULL_NAME = "full_name";
        private static final String PRIVATE = "private";
        private static final String DESCRIPTION = "description";
        private static final String CREATED_AT = "created_at";
        private static final String STARGAZERS_COUNT = "stargazers_count";
        private static final String OWNER_ID = "owner_id";

        public static final Uri REPOSITORY_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(REPOSITORY_URI_PATH)
                .build();
    }

    public static class SearchResult implements BaseColumns {

        public static final String TABLE_NAME = "search_results";
        public static final String SEARCH_URI_PATH = TABLE_NAME;

        public static final String MIMETYPE_TYPE = TABLE_NAME;

        private static final String ID = "total_count";
        private static final String INCOMPLETE_RESULTS = "incomplete_results";
        private static final String REPOSITORY_ID = "repository_id";

        public static final Uri RESULTS_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(SEARCH_URI_PATH)
                .build();
    }
}

