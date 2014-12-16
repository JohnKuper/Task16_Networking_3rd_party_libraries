package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.ItemsData;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.OwnerData;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by Dmitriy_Korobeinikov on 12/16/2014.
 */
public class RepoDetailFragment extends Fragment {

    private ImageView mAvatarImage;
    private TextView mRepoName, mRepoStars, mRepoDescription, mRepoId, mRepoFullName, mRepoOwnerLogin, mRepoOwnerId, mRepoOwnerAvatarUrl, mRepoOwnerType, mRepoOwnerSiteAdmin, mPrivate, mCreatedAt;
    private ItemsData mItemsData;

    private static final String OWNER_LOGIN_PREFIX = "Login: ";
    private static final String OWNER_ID_PREFIX = "ID: ";
    private static final String OWNER_AVATAR_URL_PREFIX = "Avatar URL: ";
    private static final String OWNER_TYPE_PREFIX = "Type: ";
    private static final String OWNER_SITE_ADMIN_PREFIX = "Site admin: ";
    private static final String OTHER_DETAIL_PRIVATE_PREFIX = "Private: ";
    private static final String OTHER_DETAIL_CREATED_AT_PREFIX = "Created at: ";
    public static final String REPO_DATA = "REPO_DATA";

    public static RepoDetailFragment newInstance(ItemsData itemsData) {
        Bundle args = new Bundle();
        args.putParcelable(REPO_DATA, Parcels.wrap(itemsData));

        RepoDetailFragment fragment = new RepoDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemsData = Parcels.unwrap(getArguments().getParcelable(REPO_DATA));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_details, container, false);

        mAvatarImage = (ImageView) v.findViewById(R.id.detail_repo_image);
        Picasso.with(getActivity()).load(mItemsData.getOwner().getAvatar_url()).into(mAvatarImage);

        mRepoName = (TextView) v.findViewById(R.id.detail_repo_name);
        mRepoName.setText(mItemsData.getName());

        mRepoStars = (TextView) v.findViewById(R.id.detail_repo_stars);
        mRepoStars.setText(String.valueOf(mItemsData.getStargazers_count()));

        mRepoDescription = (TextView) v.findViewById(R.id.detail_repo_discription);
        mRepoDescription.setText(mItemsData.getDescription());

        mRepoId = (TextView) v.findViewById(R.id.detail_repo_id);
        mRepoId.setText(String.valueOf(mItemsData.getId()));

        mRepoFullName = (TextView) v.findViewById(R.id.detail_repo_full_name);
        mRepoFullName.setText(mItemsData.getFull_name());

        OwnerData ownerData = mItemsData.getOwner();

        mRepoOwnerLogin = (TextView) v.findViewById(R.id.detail_owner_login);
        mRepoOwnerLogin.setText(OWNER_LOGIN_PREFIX + ownerData.getLogin());

        mRepoOwnerId = (TextView) v.findViewById(R.id.detail_owner_id);
        mRepoOwnerId.setText(OWNER_ID_PREFIX + String.valueOf(ownerData.getId()));

        mRepoOwnerAvatarUrl = (TextView) v.findViewById(R.id.detail_owner_avatar_url);
        mRepoOwnerAvatarUrl.setText(OWNER_AVATAR_URL_PREFIX + ownerData.getAvatar_url());

        mRepoOwnerType = (TextView) v.findViewById(R.id.detail_owner_type);
        mRepoOwnerType.setText(OWNER_TYPE_PREFIX + ownerData.getType());

        mRepoOwnerSiteAdmin = (TextView) v.findViewById(R.id.detail_owner_site_admin);
        mRepoOwnerSiteAdmin.setText(OWNER_SITE_ADMIN_PREFIX + String.valueOf(ownerData.isSite_admin()));

        mPrivate = (TextView) v.findViewById(R.id.detail_private);
        mPrivate.setText(OTHER_DETAIL_PRIVATE_PREFIX + String.valueOf(mItemsData.isPrivate()));

        mCreatedAt = (TextView) v.findViewById(R.id.detail_created_at);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String formatDate = formatter.format(mItemsData.getCreated_at());
        mCreatedAt.setText(OTHER_DETAIL_CREATED_AT_PREFIX + formatDate);


        return v;
    }
}
