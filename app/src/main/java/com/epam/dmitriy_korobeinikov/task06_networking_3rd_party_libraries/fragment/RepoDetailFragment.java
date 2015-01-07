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
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Owner;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
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
    private Repository mRepository;

    private static final String OWNER_LOGIN_PREFIX = "Login: ";
    private static final String OWNER_ID_PREFIX = "ID: ";
    private static final String OWNER_AVATAR_URL_PREFIX = "Avatar URL: ";
    private static final String OWNER_TYPE_PREFIX = "Type: ";
    private static final String OWNER_SITE_ADMIN_PREFIX = "Site admin: ";
    private static final String OTHER_DETAIL_PRIVATE_PREFIX = "Private: ";
    private static final String OTHER_DETAIL_CREATED_AT_PREFIX = "Created at: ";
    public static final String REPO_DATA = "REPO_DATA";

    public static RepoDetailFragment newInstance(Repository repository) {
        Bundle args = new Bundle();
        args.putParcelable(REPO_DATA, Parcels.wrap(repository));

        RepoDetailFragment fragment = new RepoDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = Parcels.unwrap(getArguments().getParcelable(REPO_DATA));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_details, container, false);

        mAvatarImage = (ImageView) v.findViewById(R.id.detail_repo_image);
        Picasso.with(getActivity()).load(mRepository.getOwner().getAvatarUrl()).into(mAvatarImage);

        mRepoName = (TextView) v.findViewById(R.id.detail_repo_name);
        mRepoName.setText(mRepository.getName());

        mRepoStars = (TextView) v.findViewById(R.id.detail_repo_stars);
        mRepoStars.setText(String.valueOf(mRepository.getStargazersCount()));

        mRepoDescription = (TextView) v.findViewById(R.id.detail_repo_discription);
        mRepoDescription.setText(mRepository.getDescription());

        mRepoId = (TextView) v.findViewById(R.id.detail_repo_id);
        mRepoId.setText(String.valueOf(mRepository.getId()));

        mRepoFullName = (TextView) v.findViewById(R.id.detail_repo_full_name);
        mRepoFullName.setText(mRepository.getFullName());

        Owner owner = mRepository.getOwner();

        mRepoOwnerLogin = (TextView) v.findViewById(R.id.detail_owner_login);
        mRepoOwnerLogin.setText(OWNER_LOGIN_PREFIX + owner.getLogin());

        mRepoOwnerId = (TextView) v.findViewById(R.id.detail_owner_id);
        mRepoOwnerId.setText(OWNER_ID_PREFIX + String.valueOf(owner.getId()));

        mRepoOwnerAvatarUrl = (TextView) v.findViewById(R.id.detail_owner_avatar_url);
        mRepoOwnerAvatarUrl.setText(OWNER_AVATAR_URL_PREFIX + owner.getAvatarUrl());

        mRepoOwnerType = (TextView) v.findViewById(R.id.detail_owner_type);
        mRepoOwnerType.setText(OWNER_TYPE_PREFIX + owner.getType());

        mRepoOwnerSiteAdmin = (TextView) v.findViewById(R.id.detail_owner_site_admin);
        mRepoOwnerSiteAdmin.setText(OWNER_SITE_ADMIN_PREFIX + String.valueOf(owner.isSiteAdmin()));

        mCreatedAt = (TextView) v.findViewById(R.id.detail_created_at);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String formatDate = formatter.format(mRepository.getCreatedAt());
        mCreatedAt.setText(OTHER_DETAIL_CREATED_AT_PREFIX + formatDate);


        return v;
    }
}
