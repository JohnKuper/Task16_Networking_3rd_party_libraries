package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dmitriy Korobeynikov on 18.01.2015.
 * RepositoriesContract defines the database of GitHub repositories-related information.
 */
public final class RepositoriesContract {

    public static final String AUTHORITY = "com.johnkuper.epam.githubcontentprovider";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.";

    //Contains information about the table in the database that describes in SearchResults.class
    public static final class SearchResultContent implements BaseColumns {

        public static final String TABLE_NAME = "search_results";

        //Columns
        public static final String TOTAL_COUNT = "total_count";
        public static final String INCOMPLETE_RESULTS = "incomplete_results";
    }

    //Contains information about the table in the database that describes in Repository.class
    public static final class RepositoryContent implements BaseColumns {

        public static final String TABLE_NAME = "repositories";
        public static final String REPOSITORY_PATH = TABLE_NAME;

        //Repository uri pattern code
        public static final int REPOSITORY_URI_PATTERN_MANY = 1;
        public static final int REPOSITORY_URI_PATTERN_ONE = 2;

        public static final String FULL_ID = TABLE_NAME + "." + _ID;
        public static final String ID_ALIAS = "repository_id";

        //Table columns
        public static final String NAME = "name";
        public static final String FULL_NAME = "full_name";
        public static final String DESCRIPTION = "description";
        public static final String CREATED_AT = "created_at";
        public static final String UPDATED_AT = "updated_at";
        public static final String STARGAZERS_COUNT = "stargazers_count";
        public static final String LANGUAGE = "language";
        public static final String OWNER_ID = "owner_id";
        public static final String TAGS_ID = "tags_id";


        public static final Uri REPOSITORIES_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(REPOSITORY_PATH)
                .build();

        //Data types
        public static final String REPOSITORY_CONTENT_TYPE = CONTENT_TYPE + AUTHORITY + "." + REPOSITORY_PATH;
        public static final String REPOSITORY_CONTENT_ITEM_TYPE = CONTENT_ITEM_TYPE + AUTHORITY + "." + REPOSITORY_PATH;
    }

    //Contains information about the table in the database that describes in Owner.class
    public static final class OwnerContent implements BaseColumns {

        public static final String TABLE_NAME = "owners";
        public static final String FULL_ID = TABLE_NAME + "." + _ID;

        //Table columns
        public static final String LOGIN = "login";
        public static final String AVATAR_URL = "avatar_url";
        public static final String TYPE = "type";
        public static final String REPOSITORY_ID = "repository_id";
    }

    //Contains information about the table in the database that describes in Tag.class
    public static final class TagContent implements BaseColumns {

        public static final String TABLE_NAME = "tags";
        public static final String TAG_PATH = TABLE_NAME;

        public static final int TAG_URI_PATTERN_MANY = 3;
        public static final int TAG_URI_PATTERN_ONE = 4;

        //Table columns
        public static final String REPOSITORY_TAG = "repository_tag";
        public static final String REPOSITORY_ID = "repository_id";

        public static final Uri TAGS_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(TAG_PATH)
                .build();

        //Data types
        public static final String TAG_CONTENT_TYPE = CONTENT_TYPE + AUTHORITY + "." + TAG_PATH;
        public static final String TAG_CONTENT_ITEM_TYPE = CONTENT_ITEM_TYPE + AUTHORITY + "." + TAG_PATH;
    }
}
