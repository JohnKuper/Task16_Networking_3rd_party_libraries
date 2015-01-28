package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.IssuesGitHubService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountHelper;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.IssuesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.IssueContent;

/**
 * Created by Dmitriy Korobeynikov on 28.01.2015.
 */
public class GithubSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = GithubSyncAdapter.class.getSimpleName();

    private AccountHelper mAccountHelper;
    private String mUserName;
    private String mRepoName;
    private IssuesGitHubService mIssuesGitHubService;

    public GithubSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountHelper = new AccountHelper(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        mUserName = PreferencesUtils.getCurrentOwnerLogin(getContext());
        mRepoName = PreferencesUtils.getCurrentRepoName(getContext());
        mIssuesGitHubService = new IssuesGitHubService(getContext());

        try {
            String authToken = mAccountHelper.getAccountToken(AccountGeneral.ACCOUNT_NAME);
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> onPerformSync Account token = " + authToken);

            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Get remote issues");
            List<Issue> remoteIssues = mIssuesGitHubService.getRepoIssues(mUserName, mRepoName);

            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Get local issues");
            List<Issue> localIssues = getLocalIssues(provider);

            handleSync(localIssues, remoteIssues);

        } catch (Exception e) {
            Log.e(RepositoriesApplication.APP_NAME, LOG_TAG + "> Error during onPerformSync", e);
        }
    }

    private void handleSync(List<Issue> localIssues, List<Issue> remoteIssues) {
        for (Issue localIssue : localIssues) {
            if (localIssue.getNumber().equals("") && localIssue.getState().equals(IssueContent.STATE_OPEN)) {
                //Found new issue that was added local manually
                Issue newRemoteIssue = updateRemoteIssuesWithNewLocal(localIssue);
                IssuesUtils.updateLocalIssueAfterCreateItOnRemoteServer(newRemoteIssue, localIssue.getAuto_id());
                continue;
            }
            if (!localIssue.getNumber().equals("")) {
                for (Issue remoteIssue : remoteIssues) {
                    if (localIssue.getNumber().equals(remoteIssue.getNumber()) && !localIssue.getState().equals(remoteIssue.getState())) {
                        syncIssuesState(localIssue, remoteIssue);
                    }
                }
            }
        }
        syncNewRemoteIssuesWithLocal(localIssues, remoteIssues);
    }

    private void syncNewRemoteIssuesWithLocal(List<Issue> localIssues, List<Issue> remoteIssues) {
        boolean isRemoteNewForLocal = true;
        for (Issue remoteIssue : remoteIssues) {
            for (Issue localIssue : localIssues) {
                if (localIssue.getTitle().equals(remoteIssue.getTitle()) && localIssue.getBody().equals(remoteIssue.getBody()) && localIssue.getNumber().equals(remoteIssue.getNumber())) {
                    isRemoteNewForLocal = false;
                }
            }
            if (isRemoteNewForLocal) {
                IssuesUtils.createLocalIssue(remoteIssue.getNumber(), remoteIssue.getTitle(), remoteIssue.getBody(), mUserName, mRepoName, remoteIssue.getState());
            }
            isRemoteNewForLocal = true;
        }
    }

    private void syncIssuesState(Issue localIssue, Issue remoteIssue) {
        Date localUpdate = localIssue.getUpdatedAt();
        Date remoteUpdate = remoteIssue.getUpdatedAt();
        int issueNumber = Integer.valueOf(localIssue.getNumber());
        String currentState;
        if (localUpdate.compareTo(remoteUpdate) > 0) {
            currentState = localIssue.getState();
            mIssuesGitHubService.updateIssue(mUserName, mRepoName, issueNumber, currentState);
        } else if (localUpdate.compareTo(remoteUpdate) < 0) {
            currentState = remoteIssue.getState();
            IssuesUtils.changeLocalIssueState(localIssue.getAuto_id(), currentState);
        }
    }

    private List<Issue> getLocalIssues(ContentProviderClient providerClient) throws
            RemoteException {
        ArrayList<Issue> localIssues = new ArrayList<>();

        String selection = IssueContent.OWNER_LOGIN + "=?" + " AND " + IssueContent.REPO_NAME + "=?";
        String[] selectionArgs = {mUserName, mRepoName};
        Cursor cursor = providerClient.query(IssueContent.ISSUES_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                localIssues.add(Issue.fromCursor(cursor));
            }
            cursor.close();
        }
        return localIssues;
    }

    private Issue updateRemoteIssuesWithNewLocal(Issue newLocalIssue) {
        Issue newRemoteIssue = mIssuesGitHubService.createIssue(mUserName, mRepoName, newLocalIssue.getTitle(), newLocalIssue.getBody());
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> New remote issue = " + newRemoteIssue);
        return newRemoteIssue;
    }
}
