package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception.NoNetworkException;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by Dmitriy Korobeynikov on 12/15/2014.
 * Asynchronous REST request for retrieving data about repositories.
 */
public class GithubRepositoriesRequest extends RetrofitSpiceRequest<SearchResult, GitHub> {

    public static final String LOG_TAG = GithubRepositoriesRequest.class.getSimpleName();
    private String mKeyword;
    private int mPerPage;

    public GithubRepositoriesRequest(String keyword, int perPage) {
        super(SearchResult.class, GitHub.class);
        this.mKeyword = keyword;
        this.mPerPage = perPage;
    }

    @Override
    public SearchResult loadDataFromNetwork() throws NoNetworkException {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> loadDataFromNetwork");
        return getService().getRepos(mKeyword, mPerPage);
    }

    public String createCacheKey() {
        return "repositories." + mKeyword;
    }

}


