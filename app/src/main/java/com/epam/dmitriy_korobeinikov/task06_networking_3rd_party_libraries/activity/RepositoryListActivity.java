package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.NavigationDrawerFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoCreateIssueDialogFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoIssuesFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagRenameDialogFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagsFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.OpenTagRenameDialogListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoIssueCreateListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepositoryTagsOpenListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;


public class RepositoryListActivity extends ActionBarActivity implements RepoSelectedListener, RepositoryTagsOpenListener, OpenTagRenameDialogListener, RepoIssueCreateListener, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String LOG_TAG = RepositoryListActivity.class.getSimpleName();

    private RepoListFragment mRepoListFragment;
    private RepoTagsFragment mRepoTagsFragment;
    private RepoTagRenameDialogFragment mRepoTagRenameDialogFragment;
    private RepoDetailFragment mRepoDetailFragment;
    private RepoIssuesFragment mRepoIssuesFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mRepoListFragment = (RepoListFragment) mFragmentManager.getFragment(savedInstanceState, RepoListFragment.LOG_TAG);
            mRepoDetailFragment = (RepoDetailFragment) mFragmentManager.getFragment(savedInstanceState, RepoDetailFragment.LOG_TAG);
            mRepoTagsFragment = (RepoTagsFragment) mFragmentManager.getFragment(savedInstanceState, RepoTagsFragment.LOG_TAG);
            mRepoTagRenameDialogFragment = (RepoTagRenameDialogFragment) mFragmentManager.getFragment(savedInstanceState, RepoTagRenameDialogFragment.LOG_TAG);
            mRepoIssuesFragment = (RepoIssuesFragment) mFragmentManager.getFragment(savedInstanceState, RepoIssuesFragment.LOG_TAG);
        }

        setupNavigationDrawer();
        attachRepoListFragment();
        sendBroadcast(RepositoryBroadcastReceiver.getIncomingIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSinglePaneMode()) {
            clearBackStack();
            loadFragmentsForTwoPane();
            hideRepoDetailContainerInappropriateViews();
        } else {
            clearBackStack();
            loadFragmentsForSinglePane();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Find fragments
        mRepoListFragment = (RepoListFragment) mFragmentManager.findFragmentByTag(RepoListFragment.LOG_TAG);
        mRepoDetailFragment = (RepoDetailFragment) mFragmentManager.findFragmentByTag(RepoDetailFragment.LOG_TAG);
        mRepoTagsFragment = (RepoTagsFragment) mFragmentManager.findFragmentByTag(RepoTagsFragment.LOG_TAG);
        mRepoTagRenameDialogFragment = (RepoTagRenameDialogFragment) mFragmentManager.findFragmentByTag(RepoTagRenameDialogFragment.LOG_TAG);
        mRepoIssuesFragment = (RepoIssuesFragment) mFragmentManager.findFragmentByTag(RepoIssuesFragment.LOG_TAG);

        //Put fragments in a bundle
        putFragmentInBundle(outState, RepoListFragment.LOG_TAG, mRepoListFragment);
        putFragmentInBundle(outState, RepoDetailFragment.LOG_TAG, mRepoDetailFragment);
        putFragmentInBundle(outState, RepoTagsFragment.LOG_TAG, mRepoTagsFragment);
        putFragmentInBundle(outState, RepoTagRenameDialogFragment.LOG_TAG, mRepoTagRenameDialogFragment);
        putFragmentInBundle(outState, RepoIssuesFragment.LOG_TAG, mRepoIssuesFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_without_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(RepositoryListActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return false;
    }


    private void putFragmentInBundle(Bundle bundle, String tag, Fragment fragment) {
        if (fragment == null) {
            return;
        }
        mFragmentManager.putFragment(bundle, tag, fragment);
    }

    private void attachRepoListFragment() {
        Fragment fragment = mFragmentManager.findFragmentByTag(RepoListFragment.LOG_TAG);
        if (fragment == null) {
            fragment = new RepoListFragment();
            mFragmentManager.beginTransaction().add(R.id.repo_list_container, fragment, RepoListFragment.LOG_TAG).commit();
        }
    }

    private boolean isRepoDetailContainerEmpty() {
        return mFragmentManager.findFragmentById(R.id.repo_detail_container) == null;
    }

    private boolean isSinglePaneMode() {
        return findViewById(R.id.repo_detail_container) == null;
    }

    private void clearBackStack() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = mFragmentManager.getBackStackEntryAt(0);
            mFragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void loadFragmentsForSinglePane() {
        changeFragmentContainer(R.id.repo_list_container, mRepoDetailFragment, RepoDetailFragment.LOG_TAG, true);
        changeFragmentContainer(R.id.repo_list_container, mRepoTagsFragment, RepoTagsFragment.LOG_TAG, true);
        changeFragmentContainer(R.id.repo_list_container, mRepoTagRenameDialogFragment, RepoTagRenameDialogFragment.LOG_TAG, true);
        changeFragmentContainer(R.id.repo_list_container, mRepoIssuesFragment, RepoIssuesFragment.LOG_TAG, true);
    }

    private void loadFragmentsForTwoPane() {
        changeFragmentContainer(R.id.repo_detail_container, mRepoDetailFragment, RepoDetailFragment.LOG_TAG, false);
        changeFragmentContainer(R.id.repo_detail_container, mRepoTagsFragment, RepoTagsFragment.LOG_TAG, true);
        changeFragmentContainer(R.id.repo_detail_container, mRepoTagRenameDialogFragment, RepoTagRenameDialogFragment.LOG_TAG, true);
        changeFragmentContainer(R.id.repo_detail_container, mRepoIssuesFragment, RepoIssuesFragment.LOG_TAG, true);
    }

    private void changeFragmentContainer(int containerId, Fragment fragment, String tag, boolean addToBackStack) {
        if (fragment == null) {
            return;
        }
        mFragmentManager.beginTransaction().remove(fragment).commit();
        mFragmentManager.executePendingTransactions();
        if (addToBackStack) {
            mFragmentManager.beginTransaction().replace(containerId, fragment, tag).addToBackStack(tag).commit();
            mFragmentManager.executePendingTransactions();
        } else {
            mFragmentManager.beginTransaction().replace(containerId, fragment, tag).commit();
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public void onRepoSelected(RepositoryCursorItem repository) {
        RepoDetailFragment repoDetailFragment = RepoDetailFragment.newInstance(repository);
        if (isSinglePaneMode()) {
            mFragmentManager.beginTransaction().replace(R.id.repo_list_container, repoDetailFragment, RepoDetailFragment.LOG_TAG).addToBackStack(RepoDetailFragment.LOG_TAG).commit();
        } else {
            clearBackStack();
            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoDetailFragment, RepoDetailFragment.LOG_TAG).commit();
            mFragmentManager.executePendingTransactions();
            hideRepoDetailContainerInappropriateViews();
        }
    }

    private void hideRepoDetailContainerInappropriateViews() {
        if (!isRepoDetailContainerEmpty()) {
            findViewById(R.id.repo_detail_container_empty_message).setVisibility(View.GONE);
            findViewById(R.id.repo_detail_container).setBackgroundResource(0);
        }
    }

    @Override
    public void openRepositoryTags(int repositoryId) {
        RepoTagsFragment repoTagsFragment = RepoTagsFragment.newInstance(repositoryId);
        if (isSinglePaneMode()) {
            mFragmentManager.beginTransaction().replace(R.id.repo_list_container, repoTagsFragment, RepoTagsFragment.LOG_TAG).addToBackStack(RepoTagsFragment.LOG_TAG).commit();
        } else {
            mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, repoTagsFragment, RepoTagsFragment.LOG_TAG).addToBackStack(RepoTagsFragment.LOG_TAG).commit();
        }
    }

    @Override
    public void openTagRenameDialog(int repositoryId, String repositoryTag) {
        RepoTagRenameDialogFragment dialogFragment = RepoTagRenameDialogFragment.newInstance(repositoryId, repositoryTag);
        if (isSinglePaneMode()) {
            mFragmentManager.beginTransaction().replace(R.id.repo_list_container, dialogFragment, RepoTagRenameDialogFragment.LOG_TAG).addToBackStack(RepoTagRenameDialogFragment.LOG_TAG).commit();
        } else {
            dialogFragment.show(mFragmentManager, RepoTagRenameDialogFragment.LOG_TAG);
        }
    }

    private void setupNavigationDrawer() {
        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    private boolean isCurrentFragmentOnTopOfBackStack(String fragmentTag) {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount != 0) {
            FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            String topFragmentTag = backEntry.getName();
            return topFragmentTag.equals(fragmentTag);
        }
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                if (mFragmentManager.findFragmentById(R.id.repo_list_container) instanceof RepoListFragment) {
                    break;
                }
                clearBackStack();
                RepoListFragment repoListFragment = (RepoListFragment) mFragmentManager.findFragmentByTag(RepoListFragment.LOG_TAG);
                mFragmentManager.beginTransaction().replace(R.id.repo_list_container, repoListFragment, RepoListFragment.LOG_TAG).commit();
                break;
            case 1:
                if (isCurrentFragmentOnTopOfBackStack(RepoIssuesFragment.LOG_TAG)) {
                    break;
                }
                RepoIssuesFragment fragment = new RepoIssuesFragment();
                if (isSinglePaneMode()) {
                    mFragmentManager.beginTransaction().replace(R.id.repo_list_container, fragment, RepoIssuesFragment.LOG_TAG).addToBackStack(RepoIssuesFragment.LOG_TAG).commit();
                } else {
                    mFragmentManager.beginTransaction().replace(R.id.repo_detail_container, fragment, RepoIssuesFragment.LOG_TAG).addToBackStack(RepoIssuesFragment.LOG_TAG).commit();
                    mFragmentManager.executePendingTransactions();
                    hideRepoDetailContainerInappropriateViews();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onIssueCreate() {
        RepoCreateIssueDialogFragment issueDialogFragment = new RepoCreateIssueDialogFragment();
        issueDialogFragment.show(mFragmentManager, null);
    }
}


