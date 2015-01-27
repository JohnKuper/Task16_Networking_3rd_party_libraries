package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dmitriy Korobeynikov on 27.01.2015.
 */
public class IssuesContract {

    public static final String AUTHORITY = "com.johnkuper.epam.contentprovider.issues";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.";

    //Contains information about the table in the database that describes in Issue.class
    public static final class IssueContent implements BaseColumns {

        public static final String TABLE_NAME = "issues";
        public static final String ISSUE_PATH = TABLE_NAME;

        public static final int ISSUE_URI_PATTERN_ONE = 5;
        public static final int ISSUE_URI_PATTERN_MANY = 6;

        public static final String STATE_OPEN = "open";
        public static final String STATE_CLOSED = "closed";

        //Table columns
        public static final String ISSUE_ID = "issue_id";
        public static final String TITLE = "title";
        public static final String BODY = "body";
        public static final String STATE = "state";
        public static final String OWNER_LOGIN = "owner_login";
        public static final String REPO_NAME = "repo_name";

        public static final Uri ISSUES_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(ISSUE_PATH).build();

        //Data types
        public static final String ISSUE_CONTENT_TYPE = CONTENT_TYPE + AUTHORITY + "." + ISSUE_PATH;
        public static final String ISSUE_CONTENT_ITEM_TYPE = CONTENT_ITEM_TYPE + AUTHORITY + "." + ISSUE_PATH;
    }
}
