package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.Context;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.BaseGitHubService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.IssuesGitHubService;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.JacksonConverter;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 * Helper class for creating suitable RestAdapters.
 */
public class RetrofitHelper {

    public static GitHub getGitHubBaseRestAdapter() {
        return new BaseGitHubService(new RestAdapter.Builder()).getService();
    }

    public static GitHub getGitHubAuthRestAdapter(Context context) {
        return new IssuesGitHubService(context).getService();
    }
}
