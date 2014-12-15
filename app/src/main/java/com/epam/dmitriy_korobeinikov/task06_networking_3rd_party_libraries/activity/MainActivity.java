package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoListFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSinglePaneMode();

    }

    private void loadSinglePaneMode() {

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.repoListContainer);

        if (fragment == null) {
            fragment = new RepoListFragment();
            manager.beginTransaction().add(R.id.repoListContainer, fragment).commit();
        }
    }


}


