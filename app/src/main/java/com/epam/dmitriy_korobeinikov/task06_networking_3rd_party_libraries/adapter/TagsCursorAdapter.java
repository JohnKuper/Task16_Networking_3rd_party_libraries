package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment.RepoTagsFragment;

/**
 * Created by Dmitriy Korobeynikov on 10.01.2015.
 * Fills the list of repository's tags by data from cursor.
 */
public class TagsCursorAdapter extends CursorAdapter {

    private RepoTagsFragment mRepoTagsFragment;

    public TagsCursorAdapter(Context context, Cursor c, int flags, RepoTagsFragment fragment) {
        super(context, c, flags);
        mRepoTagsFragment = fragment;
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
                mRepoTagsFragment.deleteTag(repositoryId, repositoryTag);
            }
        });

        holder.editTagBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepoTagsFragment.showTagRenameDialog(repositoryId, repositoryTag);
            }
        });

    }

    public static class ViewHolder {
        public TextView tagName;
        public ImageButton deleteTagBtn;
        public ImageButton editTagBtn;
    }
}
