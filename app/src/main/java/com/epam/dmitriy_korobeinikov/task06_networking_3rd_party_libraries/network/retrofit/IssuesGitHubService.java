package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import android.content.ContentProvider;
import android.content.Context;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception.NoNetworkException;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.IssueCreateRequest;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.IssueUpdateRequest;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountHelper;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Dmitriy Korobeynikov on 28.01.2015.
 */
public class IssuesGitHubService extends BaseGitHubService {
    private static final String LOG_TAG = IssuesGitHubService.class.getSimpleName();

    public IssuesGitHubService(Context context) {
        super(new RestAdapter.Builder().setRequestInterceptor(new AuthRequestInterceptor(new AccountHelper(context))));
    }

    public List<Issue> getRepoIssues(String userName, String repoName) throws NoNetworkException {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> getRepoIssues");
        return getService().getRepoIssues(userName, repoName);
    }

    public Issue createIssue(String userName, String repoName, String title, String body) throws NoNetworkException {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> createIssue");
        IssueCreateRequest requestBody = new IssueCreateRequest();
        requestBody.title = title;
        requestBody.body = body;
        return getService().createIssue(userName, repoName, requestBody);
    }

    public Issue updateIssue(String userName, String repoName, int issueNumber, String state) throws NoNetworkException {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> updateIssue");
        IssueUpdateRequest requestBody = new IssueUpdateRequest();
        requestBody.state = state;
        return getService().updateIssue(userName, repoName, issueNumber, requestBody);
    }

    private static class AuthRequestInterceptor implements RequestInterceptor {
        private AccountHelper mAccountHelper;

        public AuthRequestInterceptor(AccountHelper accountHelper) {
            mAccountHelper = accountHelper;
        }

        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Authorization", "token " + mAccountHelper.getAccountToken(PreferencesUtils.getCurrentAccountName(RepositoriesApplication.getAppContext())));
        }
    }
}
