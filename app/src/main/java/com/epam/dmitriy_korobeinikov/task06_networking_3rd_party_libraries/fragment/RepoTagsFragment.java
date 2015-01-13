package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.TagsCursorAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesUtils;

/**
 * Created by Dmitriy Korobeynikov on 10.01.2015.
 * Contains all the tags of a particular repository and provides manipulating with them.
 */
public class RepoTagsFragment extends Fragment {

    public static final int TAGS_LOADER = 2;

    private int mRepositoryId;
    private EditText mEnterTags;
    private TagsCursorAdapter mTagsAdapter;

    public static RepoTagsFragment newInstance(int repositoryId) {
        Bundle args = new Bundle();
        args.putInt(TagContent.REPOSITORY_ID, repositoryId);

        RepoTagsFragment fragment = new RepoTagsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryId = getArguments().getInt(TagContent.REPOSITORY_ID);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_tags, container, false);

        mEnterTags = (EditText) v.findViewById(R.id.enter_tags_edit);
        mEnterTags.requestFocus();

        ImageButton addTags = (ImageButton) v.findViewById(R.id.tag_accept_btn);
        addTags.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RepositoriesUtils.isEditTextEmpty(mEnterTags)) {
                    String unsplitedTags = mEnterTags.getText().toString();
                    String[] splitedTags = unsplitedTags.split("\\s+");

                    for (String splitedTag : splitedTags) {
                        insertNewTag(splitedTag);
                    }
                    mEnterTags.setText("");
                    mEnterTags.requestFocus();
                }
            }
        });

        mTagsAdapter = new TagsCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, RepoTagsFragment.this);

        ListView tagsList = (ListView) v.findViewById(R.id.repo_tags_list);
        tagsList.setAdapter(mTagsAdapter);

        startTagsCursorLoader();

        return v;
    }


    private void startTagsCursorLoader() {
        Loader<Cursor> loader = getLoaderManager().getLoader(TAGS_LOADER);
        String selection = TagContent.REPOSITORY_ID + "=?";
        String[] selectionArgs = {String.valueOf(mRepositoryId)};
        if (loader != null) {
            getLoaderManager().restartLoader(TAGS_LOADER, null, new CursorLoaderListener<>(getActivity(), TagContent.TAGS_URI, mTagsAdapter, selection, selectionArgs));
        } else {
            getLoaderManager().initLoader(TAGS_LOADER, null, new CursorLoaderListener<>(getActivity(), TagContent.TAGS_URI, mTagsAdapter, selection, selectionArgs));
        }
    }

    private void insertNewTag(String tag) {
        ContentValues values = new ContentValues();
        values.put(TagContent.REPOSITORY_ID, mRepositoryId);
        values.put(TagContent.REPOSITORY_TAG, tag);
        Uri insertUri = ContentUris.withAppendedId(TagContent.TAGS_URI, mRepositoryId);
        Uri newUri = getActivity().getContentResolver().insert(insertUri, values);
        Log.d(BaseContent.LOG_TAG_TASK_06, "insertNewTag result URI: " + newUri);
    }

    public void deleteTag(int repositoryId, String repositoryTag) {
        Uri uri = ContentUris.withAppendedId(TagContent.TAGS_URI, repositoryId);
        String selection = TagContent.REPOSITORY_TAG + " LIKE ?";
        String[] selectionArgs = {repositoryTag};
        int deleteRows = getActivity().getContentResolver().delete(uri, selection, selectionArgs);
        Log.d(BaseContent.LOG_TAG_TASK_06, "delete: count = " + deleteRows);
    }

    public void showTagRenameDialog(int repositoryId, String repositoryTag) {
        RepoTagRenameDialogFragment dialogFragment = RepoTagRenameDialogFragment.newInstance(repositoryId, repositoryTag);
        dialogFragment.show(getFragmentManager(), "dialog_tag_rename");
    }
}
