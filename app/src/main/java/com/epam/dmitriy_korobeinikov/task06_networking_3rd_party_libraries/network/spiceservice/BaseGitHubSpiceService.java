package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.spiceservice;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 * Base GitHub SpiceService with common interface and api url.
 */
public class BaseGitHubSpiceService extends RetrofitJackson2SpiceService {

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(GitHub.class);
    }

    @Override
    protected String getServerUrl() {
        return RepositoriesApplication.GITHUB_API_URL;
    }
}
