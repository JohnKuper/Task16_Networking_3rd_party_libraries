package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice;

import android.app.Application;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitriy_Korobeinikov on 12/25/2014.
 * Caches the search results in the database.
 */
public class DBCacheSpiceService extends SpringAndroidSpiceService {

    @Override
    public CacheManager createCacheManager(Application application) {
        CacheManager cacheManager = new CacheManager();
        List<Class<?>> classCollection = new ArrayList<>();

        classCollection.add(Owner.class);
        classCollection.add(Repository.class);
        classCollection.add(SearchResult.class);

        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, BaseContent.DATABASE_NAME, BaseContent.DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory(application, databaseHelper, classCollection);
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }

    @Override
    public RestTemplate createRestTemplate() {
        return null;
    }
}
