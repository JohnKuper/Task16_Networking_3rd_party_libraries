package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachDetailFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_without_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(RepoDetailActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
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
