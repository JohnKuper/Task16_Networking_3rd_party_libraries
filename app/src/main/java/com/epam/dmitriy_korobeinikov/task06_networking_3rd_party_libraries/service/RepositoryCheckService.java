package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.BuildConfig;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.converter.JacksonConverter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.rest.GitHubRestImpl;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesUtils;

import org.codehaus.jackson.map.ObjectMapper;

import retrofit.RestAdapter;

/**
 * Created by Dmitriy Korobeynikov on 1/12/2015.
 */
public class RepositoryCheckService extends IntentService {

    public static final String REPO_NAME = "Task15_Activity";
    public static final String OWNER_LOGIN = "JohnKuper";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private boolean mSuccess;
    private Handler mHandler;

    public RepositoryCheckService() {
        super("RepoCheckService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(BaseContent.LOG_TAG_TASK_06, "Service create");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mSuccess) {
                    Toast.makeText(getApplicationContext(), "Service finished work with success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Service finished WITHOUT success", Toast.LENGTH_SHORT).show();
                }
            }

        });
        super.onDestroy();

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(BaseContent.LOG_TAG_TASK_06, "onHandleIntent");

        SearchResult searchResult = getUserRepository(REPO_NAME, OWNER_LOGIN);
        if (isRepositoryStargazersChanged(searchResult)) {
            Log.d(BaseContent.LOG_TAG_TASK_06, "Repository was changed!");
            Repository repository = searchResult.getSingleRepository();
            sendNotificationAboutStargazersChanged(repository);
            updateRepositoryCurrentStargazersCount(repository);
        }
        mSuccess = true;
    }

    private void updateRepositoryCurrentStargazersCount(Repository repository) {
        SharedPreferences.Editor editor = RepositoriesUtils.getSharedPreferences(getApplicationContext()).edit();
        editor.putInt(repository.getName(), repository.getStargazersCount());
        editor.apply();
    }

    private boolean isRepositoryStargazersChanged(SearchResult searchResult) {
        Repository repository = searchResult.getSingleRepository();
        if (repository != null) {
            int stargazers = repository.getStargazersCount();
            int currentStargazers = RepositoriesUtils.getSharedPreferences(getApplicationContext()).getInt(repository.getName(), -1);
            if (stargazers != currentStargazers) {
                return true;
            }
        }
        return false;
    }

    private void sendNotificationAboutStargazersChanged(Repository repository) {
        int newRepoStargazers = repository.getStargazersCount();
        int currentRepoStargazers = RepositoriesUtils.getSharedPreferences(getApplicationContext()).getInt(repository.getName(), -1);
        String contentText = "Repository with name " + repository.getName() + " was changed!";
        String subText = "Stargazer count changed from " + currentRepoStargazers + " to " + newRepoStargazers + ".";
        mNotification = new NotificationCompat.Builder(getApplicationContext()).setContentTitle("Info")
                .setContentText(contentText)
                .setSubText(subText)
                .setTicker("Repository changed!")
                .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher).build();

        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    private SearchResult getUserRepository(String repoName, String ownerLogin) {
        GitHub gitHub = GitHubRestImpl.getGitHubRestAdapter();
        String qualifiersPath = getSearchQualifiersPath(repoName, ownerLogin);
        return gitHub.getRepos(qualifiersPath, "stars", 10);
    }

    private String getSearchQualifiersPath(String repoName, String ownerLogin) {
        return repoName + "+user:" + ownerLogin;
    }
}
