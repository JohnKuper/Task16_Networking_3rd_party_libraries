package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.Repository;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.RepositoryCursorItem;

/**
 * Created by Dmitriy_Korobeinikov on 12/16/2014.
 */
public interface RepoSelectedListener {
    void onRepoSelected(RepositoryCursorItem repository);
}
