package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.OwnerContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.squareup.picasso.Picasso;

/**
 * Created by Dmitriy_Korobeinikov on 12/29/2014.
 */
public class RepoCursorAdapter extends CursorAdapter {

    private Cursor mCursor;

    public RepoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_repo_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.repoImage = (ImageView) v.findViewById(R.id.repo_image);
        holder.repoName = (TextView) v.findViewById(R.id.repo_name);
        holder.repoDescription = (TextView) v.findViewById(R.id.repo_discription);
        holder.repoCreateDate = (TextView) v.findViewById(R.id.repo_create_date);
        holder.repoStars = (TextView) v.findViewById(R.id.repo_stars);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.repoName.setText(cursor.getString(cursor.getColumnIndex(RepositoryContent.NAME)));
        holder.repoDescription.setText(cursor.getString(cursor.getColumnIndex(RepositoryContent.DESCRIPTION)));
        holder.repoCreateDate.setText(cursor.getString(cursor.getColumnIndex(RepositoryContent.CREATED_AT)));
        holder.repoStars.setText(String.valueOf(cursor.getString(cursor.getColumnIndex(RepositoryContent.STARGAZERS_COUNT))));

        Picasso.with(context).setIndicatorsEnabled(true);
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(OwnerContent.AVATAR_URL))).into(holder.repoImage);
    }

    public static class ViewHolder {
        public ImageView repoImage;
        public TextView repoName, repoDescription, repoCreateDate, repoStars;
    }
}
