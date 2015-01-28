package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.JacksonConverter;

/**
 * Created by Dmitriy Korobeynikov on 28.01.2015.
 */
public class BaseGitHubService {
    private GitHub mGithub;

    public BaseGitHubService(RestAdapter.Builder builder) {
        RestAdapter restAdapter = builder
                .setEndpoint(RepositoriesApplication.GITHUB_API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(RepositoriesApplication.APP_NAME))
                .setConverter(new JacksonConverter())
                .build();
        mGithub = restAdapter.create(GitHub.class);
    }

    public final GitHub getService() {
        return mGithub;
    }
}
