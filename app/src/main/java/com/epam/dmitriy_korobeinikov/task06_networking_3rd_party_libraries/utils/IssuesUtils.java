package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract;

/**
 * Created by Dmitriy Korobeynikov on 29.01.2015.
 */
public class IssuesUtils {

    private static final String LOG_TAG = IssuesUtils.class.getSimpleName();

    public static void updateLocalIssueAfterCreateItOnRemoteServer(Issue fromRemote, int localIssueId) {
        String newNumber = fromRemote.getNumber();
        String newUpdatedDate = RepositoriesDateUtils.getFormatDateAsString(fromRemote.getUpdatedAt(), RepositoriesDateUtils.BASE_DATE_FORMAT);
        ContentValues values = new ContentValues();
        values.put(IssuesContract.IssueContent.ISSUE_NUMBER, newNumber);
        values.put(IssuesContract.IssueContent.UPDATED_AT, newUpdatedDate);
        Uri uri = ContentUris.withAppendedId(IssuesContract.IssueContent.ISSUES_URI, localIssueId);
        RepositoriesApplication.getAppContext().getContentResolver().update(uri, values, null, null);
    }

    public static int changeLocalIssueState(int issueId, String state) {
        ContentValues values = new ContentValues();
        values.put(IssuesContract.IssueContent.STATE, state);
        values.put(IssuesContract.IssueContent.UPDATED_AT, RepositoriesDateUtils.getCurrentDate());
        Uri uri = ContentUris.withAppendedId(IssuesContract.IssueContent.ISSUES_URI, issueId);
        return RepositoriesApplication.getAppContext().getContentResolver().update(uri, values, null, null);
    }

    public static Uri createLocalIssue(String number, String title, String body, String userName, String repoName, String state) {
        ContentValues values = new ContentValues();
        values.put(IssuesContract.IssueContent.ISSUE_NUMBER, number);
        values.put(IssuesContract.IssueContent.TITLE, title);
        values.put(IssuesContract.IssueContent.BODY, body);
        values.put(IssuesContract.IssueContent.STATE, state);
        values.put(IssuesContract.IssueContent.OWNER_LOGIN, userName);
        values.put(IssuesContract.IssueContent.REPO_NAME, repoName);
        values.put(IssuesContract.IssueContent.UPDATED_AT, RepositoriesDateUtils.getCurrentDate());
        Uri newUri = RepositoriesApplication.getAppContext().getContentResolver().insert(IssuesContract.IssueContent.ISSUES_URI, values);
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> createIssue: New URI = " + newUri);
        return newUri;
    }
}
