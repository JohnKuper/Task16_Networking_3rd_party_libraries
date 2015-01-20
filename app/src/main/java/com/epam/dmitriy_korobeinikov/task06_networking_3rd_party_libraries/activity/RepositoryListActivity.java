package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagRenameDialogFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagsFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.OpenTagRenameDialogListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepositoryTagsOpenListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;

import org.parceler.Parcels;


public class RepositoryListActivity extends ActionBarActivity implements RepoSelectedListener, RepositoryTagsOpenListener, OpenTagRenameDialogListener {

    public static final String LOG_TAG = RepositoryListActivity.class.getSimpleName();
    public static final String IS_VIEWS_SHOULD_HIDE_KEY = "isViewsShouldHide";

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
        sendBroadcast(RepositoryBroadcastReceiver.getIncomingIntent());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(LOG_TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
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
            Intent i = new Intent(this, RepositoryDetailActivity.class);
            i.putExtra(RepoDetailFragment.REPO_DATA, Parcels.wrap(repository));
            startActivity(i);
        } else {
            RepoDetailFragment repoDetailFragment = RepoDetailFragment.newInstance(repository);
            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoDetailFragment, RepoDetailFragment.LOG_TAG).commit();
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
    public void openRepositoryTags(int repositoryId) {
        RepoTagsFragment repoTagsFragment = RepoTagsFragment.newInstance(repositoryId);
        mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoTagsFragment, RepoTagsFragment.LOG_TAG).commit();
    }

    @Override
    public void openTagRenameDialog(int repositoryId, String repositoryTag) {
        RepoTagRenameDialogFragment dialogFragment = RepoTagRenameDialogFragment.newInstance(repositoryId, repositoryTag);
        dialogFragment.show(mFragmentManager, RepoTagRenameDialogFragment.LOG_TAG);
    }
}


