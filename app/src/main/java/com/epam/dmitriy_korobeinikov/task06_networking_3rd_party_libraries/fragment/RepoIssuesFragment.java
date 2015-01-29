package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoIssueCreateListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.IssuesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.IssueContent;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
public class RepoIssuesFragment extends BaseFragment {

    public static final String LOG_TAG = RepoIssuesFragment.class.getSimpleName();

    private static final int ISSUES_LOADER = 3;

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
            case R.id.action_sync:
                startSync();
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

        Account account = new Account(AccountGeneral.ACCOUNT_NAME, AccountGeneral.ACCOUNT_TYPE);
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
}
