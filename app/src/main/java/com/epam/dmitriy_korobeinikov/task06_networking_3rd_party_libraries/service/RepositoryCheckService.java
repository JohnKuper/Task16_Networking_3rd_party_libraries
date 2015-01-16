package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity.SettingsActivity;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice.DBCacheSpiceService;

/**
 * Created by Dmitriy Korobeynikov on 1/12/2015.
 * Service for tracking the repository from a particular user. Repository name and
 * owner login for GitHub, should be specify in Settings.
 */
public class RepositoryCheckService extends IntentService {

    private static final int REPOSITORY_CHANGE_NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private Handler mHandler;

    public RepositoryCheckService() {
        super("RepoCheckService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(BaseContent.LOG_TAG_TASK_06, "Service create");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(BaseContent.LOG_TAG_TASK_06, "onHandleIntent");
        if (isOnline()) {
            checkRepository();
        } else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putString(SettingsActivity.PREF_CHECK_FREQUENCY_KEY, "0").apply();
            RepositoryBroadcastReceiver.cancelAlarmManager(getApplicationContext());
            SettingsActivity.setPreviousValueNever(true);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Internet connection is not available. Repositories tracking is disabled.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void checkRepository() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String repoName = preferences.getString(SettingsActivity.PREF_REPO_NAME_KEY, "null");
        String ownerLogin = preferences.getString(SettingsActivity.PREF_OWNER_LOGIN_KEY, "null");
        SearchResult searchResult = getUserRepository(repoName, ownerLogin);
        if (isRepositoryStargazersChanged(searchResult)) {
            Log.d(BaseContent.LOG_TAG_TASK_06, "Repository was changed!");
            Repository repository = searchResult.getSingleRepository();
            sendNotificationAboutStargazersChanged(repository);
            updateRepositoryCurrentStargazersCount(repository);
        }
    }

    private void updateRepositoryCurrentStargazersCount(Repository repository) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putInt(repository.getName(), repository.getStargazersCount());
        editor.apply();
    }

    private boolean isRepositoryStargazersChanged(SearchResult searchResult) {
        Repository repository = searchResult.getSingleRepository();
        if (repository != null) {
            int stargazers = repository.getStargazersCount();
            int currentStargazers = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(repository.getName(), 0);
            if (stargazers != currentStargazers) {
                return true;
            }
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "More than one repository was found. Specify the search criteria more detailed.", Toast.LENGTH_LONG).show();

                }
            });
        }
        return false;
    }

    private void sendNotificationAboutStargazersChanged(Repository repository) {
        int newRepoStargazers = repository.getStargazersCount();
        int currentRepoStargazers = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(repository.getName(), 0);
        String contentText = "Repository with name " + repository.getName() + " was changed!";
        String subText = "Stargazer count changed from " + currentRepoStargazers + " to " + newRepoStargazers + ".";
        Notification notification = new NotificationCompat.Builder(getApplicationContext()).setContentTitle("Info")
                .setContentText(contentText)
                .setSubText(subText)
                .setTicker("Repository changed!")
                .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher).build();

        mNotificationManager.notify(REPOSITORY_CHANGE_NOTIFICATION_ID, notification);
    }

    private SearchResult getUserRepository(String repoName, String ownerLogin) {
        GitHub gitHub = DBCacheSpiceService.getGitHubRestAdapter();
        String qualifiersPath = repoName + "+user:" + ownerLogin;
        return gitHub.getRepos(qualifiersPath, 10);
    }

}
