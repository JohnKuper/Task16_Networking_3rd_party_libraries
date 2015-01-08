package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content;

import android.provider.BaseColumns;

/**
 * Created by Dmitriy Korobeynikov on 09.01.2015.
 * Describes data about Tag.class table in database.
 */
public class TagContent implements BaseColumns {

    public static final String TABLE_NAME = "tags";

    //Table columns
    public static final String FULL_ID = TABLE_NAME + "." + _ID;
    public static final String REPOSITORY_TAGS = "repository_tags";
    public static final String REPOSITORY_ID = "repository_id";


}
