package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

    private ListView mRepoList;
    private RepoListAdapter mAdapter;
    private Button mSendRequest;
    private EditText mEditRequest;
    private String mLastRequestCacheKey;

    private static final String TAG = "Task06";
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private GithubSpiceRetrofitRequest mGithubRequest;

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
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_list, container, false);

        mSendRequest = (Button) v.findViewById(R.id.requestBtn);
        mSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditRequest.getText().toString().length() > 0) {
                    String request = mEditRequest.getText().toString();
                    mGithubRequest = new GithubSpiceRetrofitRequest(GeneralData.class, request);
                    mLastRequestCacheKey = mGithubRequest.createCacheKey();

                    spiceManager.execute(mGithubRequest, mLastRequestCacheKey, DurationInMillis.ONE_DAY, new GeneralDataRequestListener());

                }
            }
        });
        mEditRequest = (EditText) v.findViewById(R.id.requestEdit);

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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

//    private class RetroFitRequest extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... params) {
//            RestAdapter restAdapter = new RestAdapter.Builder()
//                    .setEndpoint(GITHUB_API_URL)
//                    .setLogLevel(RestAdapter.LogLevel.FULL)
//                    .setConverter(new JacksonConverter(new ObjectMapper()))
//                    .build();
//            GitHub gitHub = restAdapter.create(GitHub.class);
//            gitHub.getRepos(params[0], "stars", 30, new Callback<GeneralData>() {
//                @Override
//                public void success(GeneralData generalData, Response response) {
//                    Log.d(TAG, "SUCCESS >>>>>> " + generalData);
//                    if (mAdapter != null) {
//                        mAdapter.setRepoListItems(generalData.getItems());
//                        mAdapter.notifyDataSetChanged();
//                    }
//                    if (mAdapter == null) {
//                        mAdapter = new RepoListAdapter(getActivity(), generalData.getItems());
//                        mRepoList.setAdapter(mAdapter);
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Log.d(TAG, error.toString());
//                }
//            });
//            return null;
//        }
//
//    }


}
