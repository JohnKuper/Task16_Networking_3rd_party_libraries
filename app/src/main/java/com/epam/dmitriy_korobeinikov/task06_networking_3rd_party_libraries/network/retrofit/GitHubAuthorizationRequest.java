package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthBody;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthResponse;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RetrofitHelper;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import retrofit.RestAdapter;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 * Authorization to GitHub for later retrieval the token from the response.
 */
public class GitHubAuthorizationRequest extends RetrofitSpiceRequest<AuthResponse, GitHub> {

    private static final String LOG_TAG = GitHubAuthorizationRequest.class.getSimpleName();
    private String mClientId;
    private AuthBody mAuthBody;
    private String mAuthorizationHeader;

    public GitHubAuthorizationRequest(String clientId, AuthBody authBody, String authorizationHeader) {
        super(AuthResponse.class, GitHub.class);
        this.mClientId = clientId;
        this.mAuthBody = authBody;
        this.mAuthorizationHeader = authorizationHeader;
    }

    @Override
    public AuthResponse loadDataFromNetwork() throws Exception {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> loadDataFromNetwork");
        BaseGitHubService gitHubService = new BaseGitHubService(new RestAdapter.Builder());
        return gitHubService.getService().authorization(mClientId, mAuthBody, mAuthorizationHeader);
    }
}
