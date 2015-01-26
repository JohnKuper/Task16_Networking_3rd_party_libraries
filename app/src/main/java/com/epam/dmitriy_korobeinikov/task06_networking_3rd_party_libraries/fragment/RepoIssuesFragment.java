package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RetrofitHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
public class RepoIssuesFragment extends BaseFragment {

    private static final String LOG_TAG = RepoIssuesFragment.class.getSimpleName();

    public static final String TOKEN = "token 8183dab7a6f73a5e3a38a41f7f56dee0a659de8b";
    public static final String ACCOUNT_TYPE = "com.johnkuper.epam.github.repositories.oauth";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_issues, container, false);


        final Button getIssues = (Button) v.findViewById(R.id.get_issues_btn);
        getIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIssues();
            }
        });

        final Button getAccountToken = (Button) v.findViewById(R.id.get_account_token);
        getAccountToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        AccountManager accountManager = AccountManager.get(getActivity());
                        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
                        Account account = accounts[0];
                        String authToken = null;
                        try {
                            authToken = accountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, false);
                            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> AuthToken = " + authToken);
                        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                            e.printStackTrace();
                        }
                        return authToken;
                    }

                }.execute();
            }
        });
        return v;
    }

    private void getIssues() {
        new AsyncTask<Void, Void, List<Issue>>() {
            @Override
            protected List<Issue> doInBackground(Void... params) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> getIssues");
                GitHub gitHub = RetrofitHelper.getGitHubBaseRestAdapter();
                return gitHub.getRepoIssues("JohnKuper", "Task15_Activity", TOKEN);
            }

            @Override
            protected void onPostExecute(List<Issue> issues) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Issues after mapping: " + issues.toString());
            }
        }.execute();
    }
}
