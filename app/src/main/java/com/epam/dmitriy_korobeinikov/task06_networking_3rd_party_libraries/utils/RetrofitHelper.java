package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHub;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.JacksonConverter;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 * Helper class for creating suitable RestAdapters.
 */
public class RetrofitHelper {

    public static GitHub getGitHubBaseRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(RepositoriesApplication.GITHUB_API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(RepositoriesApplication.APP_NAME))
                .setConverter(new JacksonConverter())
                .build();
        return restAdapter.create(GitHub.class);
    }
}
