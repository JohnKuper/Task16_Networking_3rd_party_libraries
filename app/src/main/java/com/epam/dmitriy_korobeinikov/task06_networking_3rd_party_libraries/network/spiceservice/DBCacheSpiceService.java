package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.spiceservice;

import android.app.Application;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Tag;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesDBHelper;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy Korobeynikov on 12/25/2014.
 * Caches the search results in the database.
 */
public class DBCacheSpiceService extends BaseGitHubSpiceService {

    @Override
    public CacheManager createCacheManager(Application application) {
        CacheManager cacheManager = new CacheManager();
        List<Class<?>> classCollection = new ArrayList<>();

        classCollection.add(Owner.class);
        classCollection.add(Repository.class);
        classCollection.add(SearchResult.class);
        classCollection.add(Tag.class);

        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, RepositoriesDBHelper.DATABASE_NAME, RepositoriesDBHelper.DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory(application, databaseHelper, classCollection);
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }

}
