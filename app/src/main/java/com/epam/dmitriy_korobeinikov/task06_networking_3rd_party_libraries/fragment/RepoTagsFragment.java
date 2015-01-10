package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.BuildConfig;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.TagsCursorAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;

/**
 * Created by Dmitriy Korobeynikov on 10.01.2015.
 */
public class RepoTagsFragment extends Fragment {

    public static final int TAGS_LOADER = 2;

    private int mRepositoryId;
    private ListView tagsList;
    private EditText mEnterTags;
    private TagsCursorAdapter mTagsAdapter;
    private static final String TAG = "Task06";

    public static RepoTagsFragment newInstance(int repositoryId) {
        Bundle args = new Bundle();
        args.putInt("repositoryId", repositoryId);

        RepoTagsFragment fragment = new RepoTagsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepositoryId = getArguments().getInt("repositoryId");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                if (!isEditTextEmpty(mEnterTags)) {
                    String unsplitedTags = mEnterTags.getText().toString();
                    String[] splitedTags = unsplitedTags.split("\\s+");

                    for (int i = 0; i < splitedTags.length; i++) {
                        insertNewTag(splitedTags[i]);
                    }
                    mEnterTags.setText("");
                    mEnterTags.requestFocus();
                }
            }
        });

        mTagsAdapter = new TagsCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        tagsList = (ListView) v.findViewById(R.id.repo_tags_list);
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
        Log.d(TAG, "insertNewTag result URI: " + newUri);
    }

    private boolean isEditTextEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
