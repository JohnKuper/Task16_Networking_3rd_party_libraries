package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
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
        setMatcherController(new MatcherController()
                        .add(Owner.class)
                            .add(MimeTypeVnd.SubType.DIRECTORY, "", MyContract.CONTENT_URI_PATTERN_MANY)
                            .add(MimeTypeVnd.SubType.ITEM, "#", MyContract.CONTENT_URI_PATTERN_ONE)
                        .add(Repository.class)
                            .add(MimeTypeVnd.SubType.DIRECTORY, "", MyContract.CONTENT_URI_PATTERN_MANY)
                            .add(MimeTypeVnd.SubType.ITEM, "#", MyContract.CONTENT_URI_PATTERN_ONE)
                        .add(SearchResult.class)
                            .add(MimeTypeVnd.SubType.DIRECTORY, "", MyContract.CONTENT_URI_PATTERN_MANY)
                            .add(MimeTypeVnd.SubType.ITEM, "#", MyContract.CONTENT_URI_PATTERN_ONE)
        );
        return true;
    }
}
