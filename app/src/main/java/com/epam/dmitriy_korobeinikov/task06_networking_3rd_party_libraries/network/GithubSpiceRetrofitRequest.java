package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network;

import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by Dmitriy Korobeynikov on 12/15/2014.
 * Asynchronous rest request with Robospice and Retrofit.
 */
public class GithubSpiceRetrofitRequest extends RetrofitSpiceRequest<SearchResult, GitHub> {

    public static final String LOG_TAG = GithubSpiceRetrofitRequest.class.getSimpleName();
    private String mKeyword;
    private int mPerPage;

    public GithubSpiceRetrofitRequest(String keyword, int perPage) {
        super(SearchResult.class, GitHub.class);
        this.mKeyword = keyword;
        this.mPerPage = perPage;
    }

    @Override
    public SearchResult loadDataFromNetwork() {
        Log.d(LOG_TAG, " Call web service");
        return getService().getRepos(mKeyword, mPerPage);
    }

    public String createCacheKey() {
        return "repositories." + mKeyword;
    }

}


