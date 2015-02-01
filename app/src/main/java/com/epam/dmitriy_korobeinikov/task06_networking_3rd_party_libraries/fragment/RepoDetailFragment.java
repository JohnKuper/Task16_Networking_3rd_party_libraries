package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener.RepositoryTagsOpenListener;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * Created by Dmitriy Korobeynikov on 12/16/2014.
 * Displays detailed information about the repository and includes button
 * for open repository's tags.
 */
public class RepoDetailFragment extends BaseFragment {

    public static final String LOG_TAG = RepoDetailFragment.class.getSimpleName();
    private RepositoryCursorItem mRepository;
    private RepositoryTagsOpenListener tagsOpenListener;

    private static final String REPO_DATA = "REPO_DATA";

    public static RepoDetailFragment newInstance(RepositoryCursorItem repository) {
        Bundle args = new Bundle();
        args.putParcelable(REPO_DATA, Parcels.wrap(repository));

        RepoDetailFragment fragment = new RepoDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        tagsOpenListener = (RepositoryTagsOpenListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRepository = Parcels.unwrap(savedInstanceState.getParcelable(REPO_DATA));
        } else {
            mRepository = Parcels.unwrap(getArguments().getParcelable(REPO_DATA));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(REPO_DATA, Parcels.wrap(mRepository));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_repo_details, container, false);

        TextView repoName = (TextView) v.findViewById(R.id.detail_main_title);
        repoName.setText(mRepository.getName());

        ImageView avatarImage = (ImageView) v.findViewById(R.id.detail_repo_image);
        Picasso.with(getActivity()).load(mRepository.getAvatarUrl()).into(avatarImage);

        TextView repoLanguage = (TextView) v.findViewById(R.id.detail_repo_language);
        repoLanguage.setText(mRepository.getLanguage());

        TextView repoFullName = (TextView) v.findViewById(R.id.detail_repo_full_name);
        repoFullName.setText(mRepository.getFullName());

        TextView repoDescription = (TextView) v.findViewById(R.id.detail_repo_description);
        repoDescription.setText(mRepository.getDescription());

        TextView createdAt = (TextView) v.findViewById(R.id.detail_repo_created_at);
        createdAt.setText(mRepository.getCreatedAt());

        TextView updatedAt = (TextView) v.findViewById(R.id.detail_repo_updated_at);
        updatedAt.setText(mRepository.getUpdateAt());

        TextView repoStars = (TextView) v.findViewById(R.id.detail_repo_stargazer);
        repoStars.setText(String.valueOf(mRepository.getStargazersCount()));

        TextView repoOwnerLogin = (TextView) v.findViewById(R.id.detail_repo_login);
        repoOwnerLogin.setText(mRepository.getLogin());

        TextView repoOwnerAvatarUrl = (TextView) v.findViewById(R.id.detail_repo_avatar_url);
        repoOwnerAvatarUrl.setText(mRepository.getAvatarUrl());
        repoOwnerAvatarUrl.setSelected(true);

        TextView repoOwnerType = (TextView) v.findViewById(R.id.detail_repo_type);
        repoOwnerType.setText(mRepository.getType());

        ImageButton repoEditTags = (ImageButton) v.findViewById(R.id.edit_tags_btn);
        repoEditTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsOpenListener.openRepositoryTags(mRepository.getRepositoryId());
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActionBar().setTitle(getString(R.string.repo_details_title));
    }

    @Override
    public void onDetach() {
        tagsOpenListener = null;
        super.onDetach();
    }

}
