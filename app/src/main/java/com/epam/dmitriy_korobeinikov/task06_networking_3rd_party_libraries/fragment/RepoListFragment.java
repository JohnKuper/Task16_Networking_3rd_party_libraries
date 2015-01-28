package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
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
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.RepoCursorAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.spiceservice.DBCacheSpiceService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GithubRepositoriesRequest;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.*;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by Dmitriy Korobeynikov on 12/12/2014.
 * This class is used for search and display preview information about GitHub's repositories as list.
 */
public class RepoListFragment extends BaseFragment implements OnQueryTextListener {

    public static final String LOG_TAG = RepoListFragment.class.getSimpleName();
    public static final int REPOSITORIES_LOADER = 1;
    public static final String KEYWORD_BUNDLE_KEY = "keyWord";

    //SavedInstanceState values
    private String mKeyword;

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
    public void onResume() {
        super.onResume();
        getActionBar().setTitle(getString(R.string.activity_main_title));
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
    }

    private class GeneralDataRequestListener implements RequestListener<SearchResult> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> onRequestFailure - ", e);
            dismissProgressDialog();
        }

        @Override
        public void onRequestSuccess(SearchResult searchResult) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> onRequestSuccess");

            startRepositoriesCursorLoader();
            dismissProgressDialog();

            if (searchResult.totalCount == 0) {
                Toast.makeText(getActivity(), getString(R.string.toast_search_complete_without_results), Toast.LENGTH_SHORT).show();
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

    private void setupSearchView(Menu menu) {
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Enter keyword");

        View searchPlate = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha);

        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!mSearchView.isFocusable()) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                }

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 200) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (s.length() > 0) {
            mKeyword = s;
            GithubRepositoriesRequest mGithubRequest = new GithubRepositoriesRequest(mKeyword, 10);
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
