package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;

/**
 * Created by Dmitriy Korobeynikov on 10.01.2015.
 */
public class TagsCursorAdapter extends CursorAdapter {

    public static final String TAG = "Task06";

    public TagsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_tag_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.tagName = (TextView) v.findViewById(R.id.tag_name);
        holder.deleteTag = (ImageView) v.findViewById(R.id.tag_delete_image);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        final String repositoryTag = cursor.getString(cursor.getColumnIndex(TagContent.REPOSITORY_TAG));

        holder.tagName.setText(cursor.getString(cursor.getColumnIndex(TagContent.REPOSITORY_TAG)));
        holder.deleteTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int repositoryId = cursor.getInt(cursor.getColumnIndex(TagContent.REPOSITORY_ID));

                Uri uri = ContentUris.withAppendedId(TagContent.TAGS_URI, repositoryId);
                String selection = TagContent.REPOSITORY_TAG + " LIKE ?";
                String[] selectionArgs = {repositoryTag};
                int deleteRows = context.getContentResolver().delete(uri, selection, selectionArgs);
                Log.d(TAG, "delete: count = " + deleteRows);
            }
        });
    }


    public static class ViewHolder {
        public TextView tagName;
        public ImageView deleteTag;
    }
}
