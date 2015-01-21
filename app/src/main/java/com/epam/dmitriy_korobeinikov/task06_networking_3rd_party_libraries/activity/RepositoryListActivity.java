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

import java.util.List;


public class RepositoryListActivity extends ActionBarActivity implements RepoSelectedListener, RepositoryTagsOpenListener, OpenTagRenameDialogListener {

    public static final String LOG_TAG = RepositoryListActivity.class.getSimpleName();
    public static final String IS_VIEWS_SHOULD_HIDE_KEY = "isViewsShouldHide";

    private RepoListFragment mRepoListFragment;
    private RepoTagsFragment mRepoTagsFragment;
    private RepoTagRenameDialogFragment mRepoTagRenameDialogFragment;

    private FragmentManager mFragmentManager;
    private RepoDetailFragment mRepoDetailFragment1;

    private boolean mIsViewsShouldHide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mRepoListFragment = (RepoListFragment) mFragmentManager.getFragment(savedInstanceState, RepoListFragment.LOG_TAG);
//            mRepoDetailFragment = (RepoDetailFragment) mFragmentManager.getFragment(savedInstanceState, RepoDetailFragment.LOG_TAG);
//            mRepoTagsFragment = (RepoTagsFragment) mFragmentManager.getFragment(savedInstanceState, RepoTagsFragment.LOG_TAG);
//            mRepoTagRenameDialogFragment = (RepoTagRenameDialogFragment) mFragmentManager.getFragment(savedInstanceState, RepoTagRenameDialogFragment.LOG_TAG);

        }
        if (!isSinglePaneMode()) {
            // cleanBackStack();
            loadFragmentsForTwoPane();
            hideRepoDetailContainerInappropriateViews();
        }

        attachRepoListFragment();
        sendBroadcast(RepositoryBroadcastReceiver.getIncomingIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSinglePaneMode()) {
            hideRepoDetailContainerInappropriateViews();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Find fragments
        mRepoListFragment = (RepoListFragment) mFragmentManager.findFragmentByTag(RepoListFragment.LOG_TAG);
//        mRepoDetailFragment = (RepoDetailFragment) mFragmentManager.findFragmentByTag(RepoDetailFragment.LOG_TAG);
//        mRepoTagsFragment = (RepoTagsFragment) mFragmentManager.findFragmentByTag(RepoTagsFragment.LOG_TAG);
//        mRepoTagRenameDialogFragment = (RepoTagRenameDialogFragment) mFragmentManager.findFragmentByTag(RepoTagRenameDialogFragment.LOG_TAG);

        //Put fragments in a bundle
        mFragmentManager.putFragment(outState, RepoListFragment.LOG_TAG, mRepoListFragment);
//        if (mRepoDetailFragment != null) {
//            mFragmentManager.putFragment(outState, RepoDetailFragment.LOG_TAG, mRepoDetailFragment);
//        }
//        if (mRepoTagsFragment != null) {
//            mFragmentManager.putFragment(outState, RepoTagsFragment.LOG_TAG, mRepoTagsFragment);
//        }
//        if (mRepoTagRenameDialogFragment != null) {
//            mFragmentManager.putFragment(outState, RepoTagRenameDialogFragment.LOG_TAG, mRepoTagRenameDialogFragment);
//        }
    }

    private boolean isSinglePaneMode() {
        return findViewById(R.id.repo_detail_container) == null;
    }


    private void attachRepoListFragment() {
        Fragment fragment = mFragmentManager.findFragmentByTag(RepoListFragment.LOG_TAG);
        if (fragment == null) {
            fragment = new RepoListFragment();
            mFragmentManager.beginTransaction().add(R.id.repo_list_container, fragment, RepoListFragment.LOG_TAG).commit();
        }
    }

    private void cleanBackStack() {
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            mFragmentManager.popBackStack();
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void loadFragmentsForTwoPane() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 1) {
            RepoDetailFragment repoDetailFragment = (RepoDetailFragment) fragments.get(1);
            changeFragmentContainer(R.id.repo_detail_container, repoDetailFragment, RepoDetailFragment.LOG_TAG, false);

//            RepoTagsFragment repoTagsFragment = (RepoTagsFragment) fragments.get(2);
//            changeFragmentContainer(R.id.repo_detail_container, repoTagsFragment, RepoTagsFragment.LOG_TAG, true);

//        if (mRepoTagsFragment != null) {
//            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, mRepoTagsFragment, RepoTagsFragment.LOG_TAG).addToBackStack(null).commit();
//        }
//        if (mRepoTagRenameDialogFragment != null) {
//            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, mRepoTagRenameDialogFragment, RepoTagRenameDialogFragment.LOG_TAG).addToBackStack(null).commit();
//        }
        }
    }

    private void changeFragmentContainer(int containerId, Fragment fragment, String tag, boolean addToBackStack) {
        if (fragment != null) {
            mFragmentManager.beginTransaction().remove(fragment).commit();
            mFragmentManager.executePendingTransactions();
            Fragment copyFragment = recreateFragment(fragment);
            if (addToBackStack) {
                mFragmentManager.beginTransaction().replace(containerId, copyFragment, tag).addToBackStack(null).commit();
            } else {
                mFragmentManager.beginTransaction().replace(containerId, copyFragment, tag).commit();
            }
            mIsViewsShouldHide = true;
        }
    }

    private Fragment recreateFragment(Fragment f) {
        try {
            Fragment.SavedState savedState = mFragmentManager.saveFragmentInstanceState(f);
            Fragment newInstance = f.getClass().newInstance();
            newInstance.setInitialSavedState(savedState);

            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            Log.e(LOG_TAG, "recreateFragment", e);
            throw new RuntimeException("Cannot recreate fragment " + f.getClass().getName(), e);
        }
    }

    @Override
    public void onRepoSelected(RepositoryCursorItem repository) {
        RepoDetailFragment repoDetailFragment = RepoDetailFragment.newInstance(repository);
        if (isSinglePaneMode()) {
            mFragmentManager.beginTransaction().replace(R.id.repo_list_container, repoDetailFragment, RepoDetailFragment.LOG_TAG).addToBackStack(null).commit();
        } else {
            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoDetailFragment, RepoDetailFragment.LOG_TAG).commit();
            mIsViewsShouldHide = true;
            hideRepoDetailContainerInappropriateViews();
        }
    }

    private boolean isRepoDetailContainerEmpty() {
        return mFragmentManager.findFragmentById(R.id.repo_detail_container) == null;
    }

    private void hideRepoDetailContainerInappropriateViews() {
        if (mIsViewsShouldHide) {
            findViewById(R.id.repo_detail_container_empty_message).setVisibility(View.GONE);
            findViewById(R.id.repo_detail_container).setBackgroundResource(0);
        }
    }

    @Override
    public void openRepositoryTags(int repositoryId) {
        RepoTagsFragment repoTagsFragment = RepoTagsFragment.newInstance(repositoryId);
        if (isSinglePaneMode()) {
            mFragmentManager.beginTransaction().replace(R.id.repo_list_container, repoTagsFragment, RepoTagsFragment.LOG_TAG).addToBackStack(null).commit();
        } else {
            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoTagsFragment, RepoDetailFragment.LOG_TAG).commit();
        }
    }

    @Override
    public void openTagRenameDialog(int repositoryId, String repositoryTag) {
        RepoTagRenameDialogFragment dialogFragment = RepoTagRenameDialogFragment.newInstance(repositoryId, repositoryTag);
        dialogFragment.show(mFragmentManager, RepoTagRenameDialogFragment.LOG_TAG);
    }
}


