package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagsFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoTagsOpenListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;

import org.parceler.Parcels;


public class RepoListActivity extends ActionBarActivity implements RepoSelectedListener, RepoTagsOpenListener {

    private final String LOG_TAG = getClass().getSimpleName();
    public static final String IS_VIEWS_SHOULD_HIDE_KEY = "IS_VIEWS_SHOULD_HIDE_KEY";
    private RepoListFragment mRepoListFragment;
    private FragmentManager mFragmentManager;
    private Boolean mIsViewsShouldHide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        if (savedInstanceState != null) {
            mRepoListFragment = (RepoListFragment) getSupportFragmentManager().getFragment(savedInstanceState, RepoListFragment.LOG_TAG);
            mIsViewsShouldHide = savedInstanceState.getBoolean(IS_VIEWS_SHOULD_HIDE_KEY);
        }
        mFragmentManager = getSupportFragmentManager();
        if (!isSinglePaneMode() && mIsViewsShouldHide) {
            hideInappropriateViews();
        }


        attachRepoListFragment();
        sendBroadCastForStartCheckService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRepoListFragment = (RepoListFragment) getSupportFragmentManager().findFragmentByTag(RepoListFragment.LOG_TAG);
        getSupportFragmentManager().putFragment(outState, RepoListFragment.LOG_TAG, mRepoListFragment);
        outState.putBoolean(IS_VIEWS_SHOULD_HIDE_KEY, mIsViewsShouldHide);
    }


    private boolean isSinglePaneMode() {
        return findViewById(R.id.repo_detail_container) == null;
    }

    private void attachRepoListFragment() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.repo_list_container);
        if (fragment == null) {
            fragment = new RepoListFragment();
            mFragmentManager.beginTransaction().add(R.id.repo_list_container, fragment, RepoListFragment.LOG_TAG).commit();
        }
    }

    @Override
    public void onRepoSelected(RepositoryCursorItem repository) {
        if (isSinglePaneMode()) {
            Intent i = new Intent(this, RepoDetailActivity.class);
            i.putExtra(RepoDetailFragment.REPO_DATA, Parcels.wrap(repository));
            startActivity(i);
        } else {
            RepoDetailFragment repoDetailFragment = RepoDetailFragment.newInstance(repository);
            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoDetailFragment, RepoDetailFragment.FRAGMENT_TAG).commit();
            if (!mIsViewsShouldHide) {
                hideInappropriateViews();
                mIsViewsShouldHide = true;
            }
        }
    }

    private void hideInappropriateViews() {
        findViewById(R.id.repo_detail_container_empty_message).setVisibility(View.GONE);
        findViewById(R.id.repo_detail_container).setBackgroundResource(0);
    }

    @Override
    public void openRepoTags(int repositoryId) {
        RepoTagsFragment repoTagsFragment = RepoTagsFragment.newInstance(repositoryId);
        mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoTagsFragment, RepoTagsFragment.FRAGMENT_TAG).commit();
    }

    private void sendBroadCastForStartCheckService() {
        String checkFrequency = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SettingsActivity.PREF_CHECK_FREQUENCY_KEY, "0");
        if (!checkFrequency.equals("0")) {
            Log.d(LOG_TAG, "Send broadcast");
            Intent intent = new Intent();
            intent.setAction(RepositoryBroadcastReceiver.RECEIVER_ACTION);
            sendBroadcast(intent);
        }
    }
}


