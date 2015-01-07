package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.OwnerContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dmitriy_Korobeinikov on 12/29/2014.
 * Fills the list of GitHub's repositories by data from cursor.
 */
public class RepoCursorAdapter extends CursorAdapter {

    public static final String TAG = "Task06";

    public RepoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_repo_list, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.repoImage = (ImageView) v.findViewById(R.id.repo_image);
        holder.repoName = (TextView) v.findViewById(R.id.repo_name);
        holder.repoLanguage = (TextView) v.findViewById(R.id.repo_language);
        holder.repoUpdateDate = (TextView) v.findViewById(R.id.repo_update_date);
        holder.repoStars = (TextView) v.findViewById(R.id.repo_stars);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.repoName.setText(cursor.getString(cursor.getColumnIndex(RepositoryContent.NAME)));
        holder.repoLanguage.setText(cursor.getString(cursor.getColumnIndex(RepositoryContent.LANGUAGE)));


        holder.repoUpdateDate.setText("Updated: " + getElapsedDate(cursor, RepositoryContent.UPDATED_AT));
        holder.repoStars.setText(String.valueOf(cursor.getString(cursor.getColumnIndex(RepositoryContent.STARGAZERS_COUNT))));

        Picasso.with(context).setIndicatorsEnabled(true);
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(OwnerContent.AVATAR_URL))).into(holder.repoImage);
    }

    private String getElapsedDate(Cursor cursor, String columnName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String date = cursor.getString(cursor.getColumnIndex(columnName));
        Date updateDate = null;

        try {
            updateDate = formatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(updateDate);
    }

    public static class ViewHolder {
        public ImageView repoImage;
        public TextView repoName, repoLanguage, repoUpdateDate, repoStars;
    }
}
