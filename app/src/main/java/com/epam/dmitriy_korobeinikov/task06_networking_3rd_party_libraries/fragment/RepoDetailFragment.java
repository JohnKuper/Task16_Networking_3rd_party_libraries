package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepoTagsOpenListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * Created by Dmitriy Korobeynikov on 12/16/2014.
 * Displays detailed information about the repository and includes button
 * for open repository's tags.
 */
public class RepoDetailFragment extends Fragment {

    private RepositoryCursorItem mRepository;
    private ActionBarActivity mActivity;
    private RepoTagsOpenListener tagsOpenListener;

    public static final String REPO_DATA = "REPO_DATA";
    private static final String TAG = "Task06";

    public static RepoDetailFragment newInstance(RepositoryCursorItem repository) {
        Bundle args = new Bundle();
        args.putParcelable(REPO_DATA, Parcels.wrap(repository));

        RepoDetailFragment fragment = new RepoDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = (ActionBarActivity) activity;
        tagsOpenListener = (RepoTagsOpenListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mActivity = null;
        tagsOpenListener = null;
        super.onDetach();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = Parcels.unwrap(getArguments().getParcelable(REPO_DATA));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_details, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.repo_detail_toolbar);
        if (toolbar != null) {
            mActivity.setSupportActionBar(toolbar);
        }

        ImageView mAvatarImage = (ImageView) v.findViewById(R.id.detail_repo_image);
        Picasso.with(getActivity()).load(mRepository.getAvatarUrl()).into(mAvatarImage);

        TextView mRepoLanguage = (TextView) v.findViewById(R.id.detail_repo_language);
        mRepoLanguage.setText(mRepository.getLanguage());

        TextView mRepoName = (TextView) v.findViewById(R.id.detail_repo_name);
        mRepoName.setText(mRepository.getName());

        TextView mRepoFullName = (TextView) v.findViewById(R.id.detail_repo_full_name);
        mRepoFullName.setText(mRepository.getFullName());

        final TextView mRepoDescription = (TextView) v.findViewById(R.id.detail_repo_description);
        mRepoDescription.setText(mRepository.getDescription());

        TextView mCreatedAt = (TextView) v.findViewById(R.id.detail_repo_created_at);
        mCreatedAt.setText(mRepository.getCreatedAt());

        TextView mUpdatedAt = (TextView) v.findViewById(R.id.detail_repo_updated_at);
        mUpdatedAt.setText(mRepository.getUpdateAt());

        TextView mRepoStars = (TextView) v.findViewById(R.id.detail_repo_stars);
        mRepoStars.setText(String.valueOf(mRepository.getStargazersCount()));

        TextView mRepoOwnerLogin = (TextView) v.findViewById(R.id.detail_repo_login);
        mRepoOwnerLogin.setText(mRepository.getLogin());

        TextView mRepoOwnerAvatarUrl = (TextView) v.findViewById(R.id.detail_repo_avatar_url);
        mRepoOwnerAvatarUrl.setText(mRepository.getAvatarUrl());

        TextView mRepoOwnerType = (TextView) v.findViewById(R.id.detail_repo_type);
        mRepoOwnerType.setText(mRepository.getType());

        ImageButton mRepoEditTags = (ImageButton) v.findViewById(R.id.edit_tags_btn);
        mRepoEditTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsOpenListener.openRepoTags(mRepository.getRepositoryId());
            }
        });
        return v;
    }
}
