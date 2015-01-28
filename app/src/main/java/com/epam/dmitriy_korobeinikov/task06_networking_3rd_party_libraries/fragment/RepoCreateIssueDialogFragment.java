package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.IssuesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.PreferencesUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesDateUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.ViewsUtils;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.IssueContent;

/**
 * Created by Dmitriy Korobeynikov on 27.01.2015.
 */
public class RepoCreateIssueDialogFragment extends DialogFragment {

    public static final String LOG_TAG = RepoCreateIssueDialogFragment.class.getSimpleName();

    private EditText mIssueTitle;
    private EditText mIssueBody;

    private String mOwnerLogin;
    private String mRepoName;

    public RepoCreateIssueDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOwnerLogin = PreferencesUtils.getCurrentOwnerLogin(getActivity());
        mRepoName = PreferencesUtils.getCurrentRepoName(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_create_issue, container, false);

        mIssueTitle = (EditText) v.findViewById(R.id.issue_title_edit);
        mIssueTitle.requestFocus();
        mIssueBody = (EditText) v.findViewById(R.id.issue_body_edit);

        Button okBtn = (Button) v.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ViewsUtils.isEditTextEmpty(mIssueTitle)) {
                    String title = mIssueTitle.getText().toString();
                    String body = mIssueBody.getText().toString();
                    try {
                        IssuesUtils.createLocalIssue("", title, body, mOwnerLogin, mRepoName, IssueContent.STATE_OPEN);
                        dismiss();
                    } catch (SQLiteConstraintException e) {
                        Log.e(RepositoriesApplication.APP_NAME, LOG_TAG + "> SQLite constraint during insert ", e);
                        ViewsUtils.showToast(getActivity(), getActivity().getString(R.string.toast_only_unique_issues, title, body), Toast.LENGTH_LONG);
                    }
                } else {
                    ViewsUtils.showToast(getActivity(), getString(R.string.toast_empty_required_fields), Toast.LENGTH_LONG);
                }
            }
        });

        Button cancelBtn = (Button) v.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        setupDialogView();
        super.onResume();
    }

    private void setupDialogView() {
        getDialog().setTitle(getString(R.string.dialog_fragment_create_issue_title));
        int dialogWidth = 450;
        int dialogHeight = 350;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }


}
