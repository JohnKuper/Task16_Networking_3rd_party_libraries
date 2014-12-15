package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice;

import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.converter.JacksonConverter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.GeneralData;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.GitHub;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;

import retrofit.RestAdapter;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */
public class GithubSpiceRetrofitRequest extends SpringAndroidSpiceRequest<GeneralData> {

    private String keyword;
    private static final String TAG = "Task06";
    private static final String GITHUB_API_URL = "https://api.github.com";

    public GithubSpiceRetrofitRequest(Class<GeneralData> clazz, String keyword) {
        super(clazz);
        this.keyword = keyword;
    }

    @Override
    public GeneralData loadDataFromNetwork() throws Exception {
        Log.d(TAG, "Call web service");
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GITHUB_API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new JacksonConverter(new ObjectMapper()))
                .build();
        GitHub gitHub = restAdapter.create(GitHub.class);
        GeneralData generalData = gitHub.getRepos(keyword, "stars", 30);
        return generalData;
    }

    public String createCacheKey() {
        return "repositories." + keyword;
    }

}


