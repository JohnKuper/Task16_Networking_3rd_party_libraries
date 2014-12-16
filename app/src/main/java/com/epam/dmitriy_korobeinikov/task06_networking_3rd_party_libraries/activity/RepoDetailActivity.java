package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.ItemsData;

import org.parceler.Parcels;

/**
 * Created by Dmitriy_Korobeinikov on 12/16/2014.
 */
public class RepoDetailActivity extends ActionBarActivity {

    private ItemsData mItemsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        mItemsData = Parcels.unwrap(getIntent().getParcelableExtra(RepoDetailFragment.REPO_DATA));

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.repo_detail_container);

        if (fragment == null) {
            fragment = RepoDetailFragment.newInstance(mItemsData);
            manager.beginTransaction().add(R.id.repo_detail_container, fragment).commit();
        }

    }


}
