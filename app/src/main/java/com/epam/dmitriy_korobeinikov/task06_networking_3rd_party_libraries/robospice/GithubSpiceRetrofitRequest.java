package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice;

import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.converter.JacksonConverter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest.GitHubRestImpl;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Created by Dmitriy Korobeynikov on 12/15/2014.
 * Asynchronous rest request with Robospice and Retrofit.
 */
public class GithubSpiceRetrofitRequest extends SpringAndroidSpiceRequest<SearchResult> {

    private String keyword;

    public GithubSpiceRetrofitRequest(Class<SearchResult> clazz, String keyword) {
        super(clazz);
        this.keyword = keyword;
    }

    @Override
    public SearchResult loadDataFromNetwork() throws Exception {
        Log.d(BaseContent.LOG_TAG_TASK_06, "Call web service");
        GitHub gitHub = GitHubRestImpl.getGitHubRestAdapter();
        return gitHub.getRepos(keyword, "stars", 10);
    }

    public String createCacheKey() {
        return "repositories." + keyword;
    }

}


