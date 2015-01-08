package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dmitriy Korobeynikov on 12/26/2014.
 * Describes data about Repository.class table in database.
 */
public class RepositoryContent implements BaseColumns {

    public static final String TABLE_NAME = "repositories";
    public static final String REPOSITORY_PATH = TABLE_NAME;

    //Repository uri pattern code
    public static final int REPOSITORY_URI_PATTERN_MANY = 1;
    public static final int REPOSITORY_URI_PATTERN_ONE = 2;

    //Table columns
    public static final String FULL_ID =  TABLE_NAME + "." + _ID;
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
            .authority(BaseContent.AUTHORITY)
            .appendPath(REPOSITORY_PATH)
            .build();

    //Data types
    public static final String REPOSITORY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + BaseContent.AUTHORITY + "." + REPOSITORY_PATH;
    public static final String REPOSITORY_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + BaseContent.AUTHORITY + "." + REPOSITORY_PATH;
}
