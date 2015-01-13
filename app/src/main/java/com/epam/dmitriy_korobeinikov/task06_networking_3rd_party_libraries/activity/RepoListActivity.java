package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;

import org.parceler.Parcels;


public class RepoListActivity extends ActionBarActivity implements RepoSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);

        PreferenceManager.setDefaultValues(this, R.xml.settings, true);

        loadSinglePaneMode();

        sendBroadCastForStartCheckService();
    }

    private boolean isSinglePaneMode() {
        return findViewById(R.id.repo_detail_container) == null;
    }

    private void loadSinglePaneMode() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.repo_list_container);

        if (fragment == null) {
            fragment = new RepoListFragment();
            fragmentManager.beginTransaction().add(R.id.repo_list_container, fragment).commit();
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
        Intent intent = new Intent();
        intent.setAction(RepositoryBroadcastReceiver.RECEIVER_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(intent);
    }


}


