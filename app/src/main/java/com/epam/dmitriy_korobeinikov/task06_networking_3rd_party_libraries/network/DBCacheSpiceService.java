package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network;

import android.app.Application;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Tag;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.DBHelper;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;
import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by Dmitriy Korobeynikov on 12/25/2014.
 * Caches the search results in the database.
 */
public class DBCacheSpiceService extends RetrofitJackson2SpiceService {

    private static final String GITHUB_API_URL = "https://api.github.com";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(GitHub.class);
    }

    @Override
    protected String getServerUrl() {
        return GITHUB_API_URL;
    }

    public static GitHub getGitHubRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GITHUB_API_URL)
                .setConverter(new JacksonConverter())
                .build();
        return restAdapter.create(GitHub.class);
    }

    @Override
    public CacheManager createCacheManager(Application application) {
        CacheManager cacheManager = new CacheManager();
        List<Class<?>> classCollection = new ArrayList<>();

        classCollection.add(Owner.class);
        classCollection.add(Repository.class);
        classCollection.add(SearchResult.class);
        classCollection.add(Tag.class);

        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, DBHelper.DATABASE_NAME, DBHelper.DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory(application, databaseHelper, classCollection);
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }

}
