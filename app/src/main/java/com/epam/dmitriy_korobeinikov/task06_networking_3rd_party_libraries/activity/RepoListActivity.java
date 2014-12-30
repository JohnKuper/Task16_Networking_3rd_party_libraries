package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;

import org.parceler.Parcels;


public class RepoListActivity extends ActionBarActivity implements RepoSelectedListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSinglePaneMode();

    }

    private boolean isSinglePaneMode() {
        return findViewById(R.id.repo_detail_container) == null;
    }

    private void loadSinglePaneMode() {

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.repo_list_container);

        if (fragment == null) {
            fragment = new RepoListFragment();
            mFragmentManager.beginTransaction().add(R.id.repo_list_container, fragment).commit();
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
}


