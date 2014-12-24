package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import com.tojc.ormlite.android.OrmLiteSimpleContentProvider;
import com.tojc.ormlite.android.framework.MatcherController;
import com.tojc.ormlite.android.framework.MimeTypeVnd;

/**
 * Created by Dmitriy_Korobeinikov on 12/24/2014.
 */
public class GitHubContentProvider extends OrmLiteSimpleContentProvider<DBHelper> {

    @Override
    protected Class<DBHelper> getHelperClass() {
        return DBHelper.class;
    }

    @Override
    public boolean onCreate() {
//        setMatcherController(new MatcherController()
//                        .add(User.class, MimeTypeVnd.SubType.DIRECTORY, "", MyContract.User.CONTENT_URI_PATTERN_MANY)
//                        .add(User.class, MimeTypeVnd.SubType.ITEM, "#", MyContract.User.CONTENT_URI_PATTERN_ONE)
//        );
        return true;
    }
}
