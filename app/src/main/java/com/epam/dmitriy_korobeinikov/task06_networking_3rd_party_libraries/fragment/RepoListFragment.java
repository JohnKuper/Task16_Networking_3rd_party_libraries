package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity.SettingsActivity;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.RepoCursorAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.DBCacheSpiceService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.GithubSpiceRetrofitRequest;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.*;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by Dmitriy Korobeynikov on 12/12/2014.
 * This class is used for search and display preview information about GitHub's repositories as list.
 */
public class RepoListFragment extends Fragment implements OnQueryTextListener {

    public static final String LOG_TAG = RepoListFragment.class.getSimpleName();
    public static final int REPOSITORIES_LOADER = 1;
    public static final String KEYWORD_BUNDLE_KEY = "keyWord";
    public static final String SEARCH_VIEW_QUERY_BUNDLE_KEY = "searchViewQuery";

    //SavedInstanceState values
    private String mKeyword;
    private String mSearchViewQuery;

    private ProgressDialog mDialog;
    private RepoCursorAdapter mRepoCursorAdapter;
    private ListView mRepoList;
    private SearchView mSearchView;
    private long mLastClickTime;

    private RepoSelectedListener mRepoSelectedListener;
    protected SpiceManager spiceManager = new SpiceManager(DBCacheSpiceService.class);

    @Override
    public void onAttach(Activity activity) {
        mRepoSelectedListener = (RepoSelectedListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mKeyword = savedInstanceState.getString(KEYWORD_BUNDLE_KEY);
            mSearchViewQuery = savedInstanceState.getString(SEARCH_VIEW_QUERY_BUNDLE_KEY);

        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");

        View v = inflater.inflate(R.layout.fragment_repo_list, container, false);

        mRepoList = (ListView) v.findViewById(R.id.repo_list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mRepoCursorAdapter == null) {
            mRepoCursorAdapter = new RepoCursorAdapter(getActivity(), null);
        }

        mRepoList.setAdapter(mRepoCursorAdapter);
        mRepoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepositoryCursorItem cursorItem = new RepositoryCursorItem();
                cursorItem.parseDataFromCursor((Cursor) mRepoCursorAdapter.getItem(position));
                mRepoSelectedListener.onRepoSelected(cursorItem);
            }
        });
        mRepoList.setEmptyView(getActivity().findViewById(R.id.empty));

        if (mKeyword != null) {
            startRepositoriesCursorLoader();
            Log.d(LOG_TAG, "startRepositoriesCursorLoader()");
        }

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
    public void onDetach() {
        mRepoSelectedListener = null;
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState()");
        outState.putString(KEYWORD_BUNDLE_KEY, mKeyword);
        outState.putString(SEARCH_VIEW_QUERY_BUNDLE_KEY, mSearchView.getQuery().toString());
    }

    private class GeneralDataRequestListener implements RequestListener<SearchResult> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d(LOG_TAG, " Request failure: ", e);
        }

        @Override
        public void onRequestSuccess(SearchResult searchResult) {
            Log.d(LOG_TAG, "<<<<<< SUCCESS >>>>>> ");

            startRepositoriesCursorLoader();
            dismissProgressDialog();

            if (searchResult.totalCount == 0) {
                Toast.makeText(getActivity(), getString(R.string.search_complete_without_results), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        mDialog = ProgressDialog.show(getActivity(), null, null, true, false);
        mDialog.setContentView(new ProgressBar(getActivity()));
    }

    private void startRepositoriesCursorLoader() {
        Loader<Cursor> loader = getLoaderManager().getLoader(REPOSITORIES_LOADER);
        String selection = RepositoryContent.DESCRIPTION + " LIKE ?" + " OR " + RepositoryContent.NAME + " LIKE ?";
        String[] selectionArgs = {"%" + mKeyword + "%", "%" + mKeyword + "%"};
        if (loader != null) {
            getLoaderManager().restartLoader(REPOSITORIES_LOADER, null, new CursorLoaderListener<>(getActivity(), RepositoryContent.REPOSITORIES_URI, mRepoCursorAdapter, selection, selectionArgs));
        } else {
            getLoaderManager().initLoader(REPOSITORIES_LOADER, null, new CursorLoaderListener<>(getActivity(), RepositoryContent.REPOSITORIES_URI, mRepoCursorAdapter, selection, selectionArgs));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_with_search, menu);
        setupSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void setupSearchView(Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.expandActionView(searchMenuItem);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Enter keyword");
        mSearchView.setQuery(mSearchViewQuery, false);


        View searchPlate = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha);


    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 200) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (s.length() > 0) {
            mKeyword = s;
            GithubSpiceRetrofitRequest mGithubRequest = new GithubSpiceRetrofitRequest(mKeyword, 10);
            String requestCacheKey = mGithubRequest.createCacheKey();
            showProgressDialog();
            spiceManager.execute(mGithubRequest, requestCacheKey, DurationInMillis.ONE_MINUTE, new GeneralDataRequestListener());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
