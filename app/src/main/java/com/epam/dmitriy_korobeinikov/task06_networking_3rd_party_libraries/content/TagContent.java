package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dmitriy Korobeynikov on 09.01.2015.
 * Describes data about Tag.class table in database.
 */
public class TagContent implements BaseColumns {

    public static final String TABLE_NAME = "tags";
    public static final String TAG_PATH = TABLE_NAME;

    public static final int TAG_URI_PATTERN_MANY = 3;
    public static final int TAG_URI_PATTERN_ONE = 4;

    //Table columns
    public static final String FULL_ID = TABLE_NAME + "." + _ID;
    public static final String REPOSITORY_TAG = "repository_tag";
    public static final String REPOSITORY_ID = "repository_id";

    public static final Uri TAGS_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(BaseContent.AUTHORITY)
            .appendPath(TAG_PATH)
            .build();

    //Data types
    public static final String TAG_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + BaseContent.AUTHORITY + "." + TAG_PATH;
    public static final String TAG_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + BaseContent.AUTHORITY + "." + TAG_PATH;


}
