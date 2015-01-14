package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesUtils;

import org.parceler.Parcels;


public class RepoListActivity extends ActionBarActivity implements RepoSelectedListener {

    private RepoListFragment mRepoListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        if (savedInstanceState != null) {
            mRepoListFragment = (RepoListFragment) getSupportFragmentManager().getFragment(savedInstanceState, RepoListFragment.FRAGMENT_TAG);
        }

        loadSinglePaneMode();

        sendBroadCastForStartCheckService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRepoListFragment = (RepoListFragment) getSupportFragmentManager().findFragmentByTag(RepoListFragment.FRAGMENT_TAG);
        getSupportFragmentManager().putFragment(outState, RepoListFragment.FRAGMENT_TAG, mRepoListFragment);
    }

    private boolean isSinglePaneMode() {
        return findViewById(R.id.repo_detail_container) == null;
    }

    private void loadSinglePaneMode() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.repo_list_container);

        if (fragment == null) {
            fragment = new RepoListFragment();
            fragmentManager.beginTransaction().add(R.id.repo_list_container, fragment, RepoListFragment.FRAGMENT_TAG).commit();
        }
    }


    @Override
    public void onRepoSelected(RepositoryCursorItem repository) {
        if (isSinglePaneMode()) {
            Intent i = new Intent(this, RepoDetailActivity.class);
            i.putExtra(RepoDetailFragment.REPO_DATA, Parcels.wrap(repository));
            startActivity(i);
        }

//        Fragment repoDetail = RepoDetailFragment.newInstance(itemsData);
//        mFragmentManager.beginTransaction().replace(R.id.repoListContainer, repoDetail).addToBackStack("detail").commit();


    }

    private void sendBroadCastForStartCheckService() {
        String checkFrequency = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SettingsActivity.PREF_CHECK_FREQUENCY_KEY, "0");
        if (!checkFrequency.equals("0")) {
            Log.d(BaseContent.LOG_TAG_TASK_06, "Send broadcast from RepoListActivity");
            Intent intent = new Intent();
            intent.setAction(RepositoryBroadcastReceiver.RECEIVER_ACTION);
            sendBroadcast(intent);
        }
    }


}


