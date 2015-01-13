package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.converter.JacksonConverter;

import org.codehaus.jackson.map.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Created by Dmitriy Korobeynikov on 1/13/2015.
 */
public class GitHubRestImpl {

    private static final String GITHUB_API_URL = "https://api.github.com";

    public static GitHub getGitHubRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GITHUB_API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new JacksonConverter(new ObjectMapper()))
                .build();
        GitHub gitHub = restAdapter.create(GitHub.class);
        return gitHub;
    }
}
