package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesDateUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.*;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.ViewsUtils;

/**
 * Created by Dmitriy Korobeynikov on 11.01.2015.
 * Used to change the tag for repository.
 */
public class RepoTagRenameDialogFragment extends DialogFragment {

    private String mRepositoryTag;
    private int mRepositoryId;
    private EditText mTagEdit;

    public RepoTagRenameDialogFragment() {
    }

    public static RepoTagRenameDialogFragment newInstance(int repositoryId, String tag) {
        Bundle args = new Bundle();
        args.putInt(TagContent.REPOSITORY_ID, repositoryId);
        args.putString(TagContent.REPOSITORY_TAG, tag);

        RepoTagRenameDialogFragment dialogFragment = new RepoTagRenameDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryId = getArguments().getInt(TagContent.REPOSITORY_ID);
        mRepositoryTag = getArguments().getString(TagContent.REPOSITORY_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_tag_rename, container, false);

        getDialog().setTitle("Edit tag");

        mTagEdit = (EditText) v.findViewById(R.id.tag_edit_text);
        mTagEdit.setText(mRepositoryTag);
        mTagEdit.setSelection(mTagEdit.getText().length());

        Button okBtn = (Button) v.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ViewsUtils.isEditTextEmpty(mTagEdit)) {
                    int updateRows = updateTag(mTagEdit.getText().toString());
                    if (updateRows != -1) {
                        dismiss();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_empty_tag_name_after_renaming), Toast.LENGTH_LONG).show();
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

    private int updateTag(String newTag) {
        ContentValues values = new ContentValues();
        values.put(TagContent.REPOSITORY_TAG, newTag);
        String selection = TagContent.REPOSITORY_TAG + " LIKE ?";
        String[] selectionArgs = {mRepositoryTag};
        Uri uri = ContentUris.withAppendedId(TagContent.TAGS_URI, mRepositoryId);
        return getActivity().getContentResolver().update(uri, values, selection, selectionArgs);
    }
}
