package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.CursorLoaderListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.OpenTagRenameDialogListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.TagContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.ViewsUtils;

/**
 * Created by Dmitriy Korobeynikov on 10.01.2015.
 * Contains all the tags of a particular repository and provides manipulating with them.
 */
public class RepoTagsFragment extends Fragment {

    public static final String LOG_TAG = RepoTagsFragment.class.getSimpleName();
    private static final int TAGS_LOADER = 2;
    private static final String KEY_REPOSITORY_ID = "KEY_REPOSITORY_ID";

    private int mRepositoryId;
    private EditText mEnterTags;
    private TagsCursorAdapter mTagsAdapter;
    private OpenTagRenameDialogListener mOpenTagRenameDialogListener;

    public static RepoTagsFragment newInstance(int repositoryId) {
        Bundle args = new Bundle();
        args.putInt(TagContent.REPOSITORY_ID, repositoryId);

        RepoTagsFragment fragment = new RepoTagsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mOpenTagRenameDialogListener = (OpenTagRenameDialogListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRepositoryId = savedInstanceState.getInt(KEY_REPOSITORY_ID);
        } else {
            mRepositoryId = getArguments().getInt(TagContent.REPOSITORY_ID);
        }
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
                if (!ViewsUtils.isEditTextEmpty(mEnterTags)) {
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

        mTagsAdapter = new TagsCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ListView tagsList = (ListView) v.findViewById(R.id.repo_tags_list);
        tagsList.setAdapter(mTagsAdapter);

        startTagsCursorLoader();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_REPOSITORY_ID, mRepositoryId);
    }

    @Override
    public void onDetach() {
        mOpenTagRenameDialogListener = null;
        super.onDetach();
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
        Log.d(LOG_TAG, "insertNewTag result URI: " + newUri);
    }

    public void deleteTag(int repositoryId, String repositoryTag) {
        Uri uri = ContentUris.withAppendedId(TagContent.TAGS_URI, repositoryId);
        String selection = TagContent.REPOSITORY_TAG + " LIKE ?";
        String[] selectionArgs = {repositoryTag};
        int deleteRows = getActivity().getContentResolver().delete(uri, selection, selectionArgs);
        Log.d(LOG_TAG, "delete count = " + deleteRows);
    }

    public void showTagRenameDialog(int repositoryId, String repositoryTag) {
        mOpenTagRenameDialogListener.openTagRenameDialog(repositoryId, repositoryTag);
    }

    /**
     * Fills the list of repository's tags by data from cursor.
     */
    private class TagsCursorAdapter extends CursorAdapter {

        public TagsCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.row_tag_list, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.tagName = (TextView) v.findViewById(R.id.tag_name);
            holder.deleteTagBtn = (ImageButton) v.findViewById(R.id.tag_delete_btn);
            holder.editTagBtn = (ImageButton) v.findViewById(R.id.tag_edit_btn);
            v.setTag(holder);
            return v;
        }

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();

            final String repositoryTag = cursor.getString(cursor.getColumnIndex(TagContent.REPOSITORY_TAG));
            final int repositoryId = cursor.getInt(cursor.getColumnIndex(TagContent.REPOSITORY_ID));

            holder.tagName.setText(cursor.getString(cursor.getColumnIndex(TagContent.REPOSITORY_TAG)));
            holder.deleteTagBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTag(repositoryId, repositoryTag);
                }
            });

            holder.editTagBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTagRenameDialog(repositoryId, repositoryTag);
                }
            });

        }

        public class ViewHolder {
            public TextView tagName;
            public ImageButton deleteTagBtn;
            public ImageButton editTagBtn;
        }
    }
}
