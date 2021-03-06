package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception.NoNetworkException;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RetrofitHelper;

/**
 * Created by Dmitriy Korobeynikov on 1/12/2015.
 * Service for tracking the repository from a particular user. Repository name and
 * owner login for GitHub, should be specify in Settings.
 */
public class RepositoryCheckService extends IntentService {

    public static final String LOG_TAG = RepositoryCheckService.class.getSimpleName();

    private static final int REPOSITORY_CHANGE_NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private Handler mHandler;
    private Context mContext;

    public RepositoryCheckService() {
        super("RepoCheckService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service create");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mHandler = new Handler();
        mContext = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");
        if (isOnline()) {
            try {
                checkRepository();
            } catch (NoNetworkException e) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Internet connection error", e);
            }
        } else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
            editor.putString(SettingsActivity.PREF_CHECK_FREQUENCY_KEY, "0").apply();
            SettingsActivity.setPreviousValueNever(true);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void checkRepository() throws NoNetworkException {
        String repoName = PreferencesUtils.getCurrentRepoName(mContext);
        String ownerLogin = PreferencesUtils.getCurrentOwnerLogin(mContext);
        SearchResult searchResult = getUserRepository(repoName, ownerLogin);
        if (isRepositoryStargazersChanged(searchResult)) {
            Log.d(LOG_TAG, " Repository was changed!");
            Repository repository = searchResult.getSingleRepository();
            sendNotificationAboutStargazersChanged(repository);
            PreferencesUtils.updateRepositoryCurrentStargazersCount(mContext, repository);
        }
    }

    private boolean isRepositoryStargazersChanged(SearchResult searchResult) {
        Repository repository = searchResult.getSingleRepository();
        if (repository != null) {
            int stargazers = repository.getStargazersCount();
            int currentStargazers = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(repository.getName(), 0);
            if (stargazers != currentStargazers) {
                return true;
            }
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, getString(R.string.toast_more_than_one_repository_found_by_check_service), Toast.LENGTH_LONG).show();
                }
            });
        }
        return false;
    }

    private void sendNotificationAboutStargazersChanged(Repository repository) {
        int newRepoStargazers = repository.getStargazersCount();
        int currentRepoStargazers = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(repository.getName(), 0);
        String contentText = getString(R.string.service_repository_was_changed, repository.getName());
        String subText = getString(R.string.service_stargazer_count_changed, currentRepoStargazers, newRepoStargazers);

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(mContext)
                .setContentTitle(getString(R.string.service_notification_content_title))
                .setContentText(contentText)
                .setSubText(subText)
                .setContentIntent(pendingIntent)
                .setTicker(getString(R.string.service_notification_ticker))
                .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher).build();

        mNotificationManager.notify(REPOSITORY_CHANGE_NOTIFICATION_ID, notification);
    }

    private SearchResult getUserRepository(String repoName, String ownerLogin) throws NoNetworkException {
        GitHub gitHub = RetrofitHelper.getGitHubBaseRestAdapter();
        String qualifiersPath = repoName + "+user:" + ownerLogin;
        return gitHub.getRepos(qualifiersPath, 10);
    }

}
