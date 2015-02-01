package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoIssueCreateListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountHelper;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.IssuesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.ViewsUtils;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.IssueContent;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
public class RepoIssuesFragment extends BaseFragment {

    public static final String LOG_TAG = RepoIssuesFragment.class.getSimpleName();
    public static final String SYNC_ACTION_START = "com.johnkuper.epam.action.SYNC_START";
    public static final String SYNC_ACTION_STOP = "com.johnkuper.epam.action.SYNC_STOP";

    private static final int ISSUES_LOADER = 3;

    private RepoIssueCreateListener mIssueCreateListener;
    private IssuesCursorAdapter mIssuesCursorAdapter;
    private AccountHelper mAccountHelper;
    private ProgressDialog mDialog;

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
        mAccountHelper = new AccountHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_issues, container, false);

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
        registerBroadcastSyncListener();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(syncListenerReceiver);
    }

    private void registerBroadcastSyncListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_ACTION_START);
        filter.addAction(SYNC_ACTION_STOP);
        getActivity().registerReceiver(syncListenerReceiver, filter);
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
                if (mAccountHelper.isAtLeastOneAccount(AccountGeneral.ACCOUNT_TYPE)) {
                    mIssueCreateListener.onIssueCreate();
                } else {
                    ViewsUtils.showToast(getActivity(), getString(R.string.please_log_in_first), Toast.LENGTH_LONG);
                }
                return true;
            case R.id.action_sync:
                if (mAccountHelper.isAtLeastOneAccount(AccountGeneral.ACCOUNT_TYPE)) {
                    startSync();
                } else {
                    ViewsUtils.showToast(getActivity(), getString(R.string.please_log_in_first), Toast.LENGTH_LONG);
                }
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

    private void startSync() {

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        Account account = new Account(PreferencesUtils.getCurrentAccountName(getActivity()), AccountGeneral.ACCOUNT_TYPE);
        ContentResolver.requestSync(account, IssuesContract.AUTHORITY, bundle);
    }

    private void getCurrentPreferences() {
        mOwnerLogin = PreferencesUtils.getCurrentAccountName(getActivity());
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

    private void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        mDialog = ProgressDialog.show(getActivity(), null, null, true, false);
        mDialog.setContentView(new ProgressBar(getActivity()));
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
                    IssuesUtils.changeLocalIssueState(issueId, IssueContent.STATE_CLOSED);
                }
            });
        }

        public class ViewHolder {
            public TextView issueTitle;
            public TextView issueBody;
            public ImageButton closeIssueBtn;
        }
    }

    private BroadcastReceiver syncListenerReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SYNC_ACTION_START)) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Sync started");
                showProgressDialog();
            } else if (intent.getAction().equals(SYNC_ACTION_STOP)) {
                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Sync finished");
                dismissProgressDialog();
            }
        }


    };
}
