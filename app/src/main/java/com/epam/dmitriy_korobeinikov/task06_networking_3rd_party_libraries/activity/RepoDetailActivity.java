package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoDetailFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagsFragment;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoTagsOpenListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;

import org.parceler.Parcels;

/**
 * Created by Dmitriy Korobeynikov on 12/16/2014.
 * Displays detail information about the repository.
 */
public class RepoDetailActivity extends ActionBarActivity implements RepoTagsOpenListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        attachDetailFragment();
    }

    private void attachDetailFragment() {

        RepositoryCursorItem repository = Parcels.unwrap(getIntent().getParcelableExtra(RepoDetailFragment.REPO_DATA));

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.repo_detail_container);

        if (fragment == null) {
            fragment = RepoDetailFragment.newInstance(repository);
            fragmentManager.beginTransaction().add(R.id.repo_detail_container, fragment).commit();
        }
    }

    private void attachTagsFragment(int repositoryId) {

        RepoTagsFragment tagsFragment = RepoTagsFragment.newInstance(repositoryId);
        fragmentManager.beginTransaction().replace(R.id.repo_detail_container, tagsFragment, "RepoTagsFragment").addToBackStack("replace_on_repo_tags_fragment").commit();
    }

    @Override
    public void openRepoTags(int repositoryId) {
        attachTagsFragment(repositoryId);
    }


}
