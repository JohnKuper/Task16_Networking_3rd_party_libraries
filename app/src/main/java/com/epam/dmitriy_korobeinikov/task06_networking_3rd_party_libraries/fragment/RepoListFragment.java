package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
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
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoSelectedListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice.DBCacheSpiceService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.robospice.GithubSpiceRetrofitRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by Dmitriy Korobeynikov on 12/12/2014.
 * This class is used for search and display preview information about GitHub's repositories as list.
 */
public class RepoListFragment extends Fragment implements OnQueryTextListener {

    public static final int REPOSITORIES_LOADER = 1;

    private ProgressDialog mDialog;
    private long mLastClickTime;
    private RepoCursorAdapter mRepoCursorAdapter;
    private String mKeyword;

    private RepoSelectedListener mRepoSelectedListener;
    protected SpiceManager spiceManager = new SpiceManager(DBCacheSpiceService.class);

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
        mRepoSelectedListener = (RepoSelectedListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mRepoSelectedListener = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_list, container, false);

        mRepoCursorAdapter = new RepoCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ListView mRepoList = (ListView) v.findViewById(R.id.repo_list);
        mRepoList.setAdapter(mRepoCursorAdapter);
        mRepoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepositoryCursorItem cursorItem = new RepositoryCursorItem();
                cursorItem.parseDataFromCursor((Cursor) mRepoCursorAdapter.getItem(position));
                mRepoSelectedListener.onRepoSelected(cursorItem);
            }
        });

        return v;
    }

    private class GeneralDataRequestListener implements RequestListener<SearchResult> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d(BaseContent.LOG_TAG_TASK_06, "Request failure: ", e);
        }

        @Override
        public void onRequestSuccess(SearchResult searchResult) {
            Log.d(BaseContent.LOG_TAG_TASK_06, "<<<<<< SUCCESS >>>>>> ");

            startRepositoriesCursorLoader();
            dismissProgressDialog();

            if (searchResult.totalCount == 0) {
                Toast.makeText(getActivity(), "Search is complete. There are no results to display", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.menu_with_search, menu);
        setupSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupSearchView(Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Enter keyword");

        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
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
            GithubSpiceRetrofitRequest mGithubRequest = new GithubSpiceRetrofitRequest(SearchResult.class, s);
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
