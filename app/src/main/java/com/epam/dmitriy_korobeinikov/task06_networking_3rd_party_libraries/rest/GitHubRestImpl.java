package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by Dmitriy Korobeynikov on 1/13/2015.
 * Creates GitHub rest adapter.
 */
public class GitHubRestImpl {

    private static final String GITHUB_API_URL = "https://api.github.com";

    public static GitHub getGitHubRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GITHUB_API_URL)
                .setConverter(new JacksonConverter())
                .build();
        return restAdapter.create(GitHub.class);
    }
}
