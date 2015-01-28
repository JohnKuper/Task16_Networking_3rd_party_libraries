package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoIssueCreateListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.IssuesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesDateUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RetrofitHelper;

import java.io.IOException;
import java.util.List;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.*;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
public class RepoIssuesFragment extends BaseFragment {

    public static final String LOG_TAG = RepoIssuesFragment.class.getSimpleName();

    private static final int ISSUES_LOADER = 3;

    public static final String TOKEN = "token 8183dab7a6f73a5e3a38a41f7f56dee0a659de8b";

    private RepoIssueCreateListener mIssueCreateListener;
    private IssuesCursorAdapter mIssuesCursorAdapter;
    private String mOwnerLogin;
    private String mRepoName;

    @Override
    public void onAttach(Activity activity) {
        mIssueCreateListener = (RepoIssueCreateListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_issues, container, false);


        final Button getIssues = (Button) v.findViewById(R.id.get_issues_btn);
        getIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getIssues();
                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true); // Performing a sync no matter if it's off
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true); // Performing a sync no matter if it's off

                Account account = new Account(AccountGeneral.ACCOUNT_NAME, AccountGeneral.ACCOUNT_TYPE);
                ContentResolver.requestSync(account, IssuesContract.AUTHORITY, bundle);
            }
        });

        final Button getAccountToken = (Button) v.findViewById(R.id.get_account_token);
        getAccountToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        AccountManager accountManager = AccountManager.get(getActivity());
                        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
                        Account account = accounts[0];
                        String authToken = null;
                        try {
                            authToken = accountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, false);
                            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> AuthToken = " + authToken);
                        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
                            e.printStackTrace();
                        }
                        return authToken;
                    }
                }.execute();
            }
        });

        mIssuesCursorAdapter = new IssuesCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ListView issuesList = (ListView) v.findViewById(R.id.repo_issues_list);
        issuesList.setAdapter(mIssuesCursorAdapter);

        getCurrentPreferences();
        startIssuesCursorLoader();
        return v;
    }

    @Override
    public void onResume() {
        getActionBar().setTitle(getString(R.string.fragment_repo_issues_title));
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_issue, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_issue:
                mIssueCreateListener.onIssueCreate();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDetach() {
        mIssueCreateListener = null;
        super.onDetach();
    }

    private void getIssues() {
        new AsyncTask<Void, Void, List<Issue>>() {
            @Override
            protected List<Issue> doInBackground(Void... params) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> getIssues");
                GitHub gitHub = RetrofitHelper.getGitHubBaseRestAdapter();
                return gitHub.getRepoIssues("JohnKuper", "Task15_Activity");
            }

            @Override
            protected void onPostExecute(List<Issue> issues) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Issues after mapping: " + issues.toString());
            }
        }.execute();
    }

    private void getCurrentPreferences() {
        mOwnerLogin = PreferencesUtils.getCurrentOwnerLogin(getActivity());
        mRepoName = PreferencesUtils.getCurrentRepoName(getActivity());
    }

    private void startIssuesCursorLoader() {
        Loader<Cursor> loader = getLoaderManager().getLoader(ISSUES_LOADER);
        String selection = IssueContent.OWNER_LOGIN + "=?" + " AND " + IssueContent.REPO_NAME + "=?" + " AND " + IssueContent.STATE + "=?";
        String[] selectionArgs = {mOwnerLogin, mRepoName, IssueContent.STATE_OPEN};
        if (loader != null) {
            getLoaderManager().restartLoader(ISSUES_LOADER, null, new CursorLoaderListener<>(getActivity(), IssueContent.ISSUES_URI, mIssuesCursorAdapter, selection, selectionArgs));
        } else {
            getLoaderManager().initLoader(ISSUES_LOADER, null, new CursorLoaderListener<>(getActivity(), IssueContent.ISSUES_URI, mIssuesCursorAdapter, selection, selectionArgs));
        }
    }

    /**
     * Fills the list of repository's issues by data from cursor.
     */
    private class IssuesCursorAdapter extends CursorAdapter {

        public IssuesCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.row_issue_list, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.issueTitle = (TextView) v.findViewById(R.id.issue_title);
            holder.issueBody = (TextView) v.findViewById(R.id.issue_body);
            holder.closeIssueBtn = (ImageButton) v.findViewById(R.id.issue_close_btn);
            v.setTag(holder);
            return v;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();

            final String issueTitle = cursor.getString(cursor.getColumnIndex(IssueContent.TITLE));
            final String issueBody = cursor.getString(cursor.getColumnIndex(IssueContent.BODY));
            final int issueId = cursor.getInt(cursor.getColumnIndex(IssueContent._ID));

            holder.issueTitle.setText(issueTitle);
            holder.issueBody.setText(issueBody);
            holder.closeIssueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IssuesUtils.changeLocalIssueState(issueId,IssueContent.STATE_CLOSED);
                }
            });
        }

        public class ViewHolder {
            public TextView issueTitle;
            public TextView issueBody;
            public ImageButton closeIssueBtn;
        }
    }
}
