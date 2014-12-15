package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.RepoListAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.GeneralData;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice.GithubSpiceRetrofitRequest;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by Dmitriy_Korobeinikov on 12/12/2014.
 */
public class RepoListFragment extends Fragment implements OnQueryTextListener {

    private static final String TAG = "Task06";

    private ListView mRepoList;
    private RepoListAdapter mAdapter;
    private String mLastRequestCacheKey;
    private ActionBarActivity mActivity;

    private GithubSpiceRetrofitRequest mGithubRequest;
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();

    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = (ActionBarActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_list, container, false);


        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        if (toolbar != null) {
            mActivity.setSupportActionBar(toolbar);
        }

        mRepoList = (ListView) v.findViewById(R.id.repoList);

        return v;
    }

    private class GeneralDataRequestListener implements RequestListener<GeneralData> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d(TAG, e.toString());
        }

        @Override
        public void onRequestSuccess(GeneralData generalData) {
            Log.d(TAG, "SUCCESS >>>>>> " + generalData);
            if (mAdapter != null) {
                mAdapter.setRepoListItems(generalData.getItems());
                mAdapter.notifyDataSetChanged();
            }
            if (mAdapter == null) {
                mAdapter = new RepoListAdapter(getActivity(), generalData.getItems());
                mRepoList.setAdapter(mAdapter);
            }
            if (generalData.getItems().size() == 0) {
                Toast.makeText(getActivity(), "Search is complete. There are no results to display", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        setupSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupSearchView(Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search for repositories");

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s.length() > 0) {
            mGithubRequest = new GithubSpiceRetrofitRequest(GeneralData.class, s);
            mLastRequestCacheKey = mGithubRequest.createCacheKey();

            spiceManager.execute(mGithubRequest, mLastRequestCacheKey, DurationInMillis.ONE_MINUTE, new GeneralDataRequestListener());

        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
