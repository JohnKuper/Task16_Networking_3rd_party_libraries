package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.TagContent;

import java.util.zip.Inflater;

/**
 * Created by Dmitriy Korobeynikov on 10.01.2015.
 */
public class TagsCursorAdapter extends CursorAdapter {

    public TagsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_tag_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.tagName = (TextView) v.findViewById(R.id.tag_name);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tagName.setText(cursor.getString(cursor.getColumnIndex(TagContent.REPOSITORY_TAG)));
    }

    public static class ViewHolder {
        public TextView tagName;
    }
}
