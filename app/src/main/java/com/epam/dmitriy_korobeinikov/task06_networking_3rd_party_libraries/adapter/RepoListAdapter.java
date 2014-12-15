package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.ItemsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dmitriy_Korobeinikov on 12/12/2014.
 * Fills in the data list of repositories preview.
 */
public class RepoListAdapter extends ArrayAdapter<ItemsData> {

    private LayoutInflater mInflater;
    private ArrayList<ItemsData> mRepoListItems;

    public RepoListAdapter(Context context, ArrayList<ItemsData> repositories) {
        super(context, android.R.layout.simple_list_item_1, repositories);
        mInflater = LayoutInflater.from(context);
        this.mRepoListItems = repositories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_repo_list, parent, false);
            holder = new ViewHolder();
            holder.repoImage = (ImageView) convertView.findViewById(R.id.repo_image);
            holder.repoName = (TextView) convertView.findViewById(R.id.repo_name);
            holder.repoDescription = (TextView) convertView.findViewById(R.id.repo_discription);
            holder.repoCreateDate = (TextView) convertView.findViewById(R.id.repo_create_date);
            holder.repoStars = (TextView) convertView.findViewById(R.id.repo_stars);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.arrow_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemsData itemData = getItem(position);
        holder.repoName.setText(itemData.getName());
        holder.repoDescription.setText(itemData.getDescription());
        holder.repoCreateDate.setText(itemData.getCreated_at());
        holder.repoStars.setText(String.valueOf(itemData.getStargazers_count()));

        Picasso.with(getContext()).load(itemData.getOwner().getAvatar_url()).into(holder.repoImage);

        return convertView;
    }

    public static class ViewHolder {
        public ImageView repoImage, rightArrow;
        public TextView repoName, repoDescription, repoCreateDate, repoStars;
    }

    public void setRepoListItems(ArrayList<ItemsData> repoListItems) {
        mRepoListItems = repoListItems;
    }

    @Override
    public int getCount() {
        return mRepoListItems.size();
    }

    @Override
    public ItemsData getItem(int position) {
        return mRepoListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
