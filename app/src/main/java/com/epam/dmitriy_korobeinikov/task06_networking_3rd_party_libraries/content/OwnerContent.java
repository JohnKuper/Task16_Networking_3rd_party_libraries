package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content;

import android.provider.BaseColumns;

/**
 * Created by Dmitriy_Korobeinikov on 12/26/2014.
 * Describes data about Owner.class table in database.
 */
public class OwnerContent implements BaseColumns {

    public static final String TABLE_NAME = "owners";

    //Table columns
    public static final String LOGIN = "login";
    public static final String AVATAR_URL = "avatar_url";
    public static final String TYPE = "type";
    public static final String SITE_ADMIN = "site_admin";
    public static final String REPOSITORY_ID = "repository_id";
}
