package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener;

/**
 * Created by Dmitriy Korobeynikov on 1/19/2015.
 * Used for open RepoTagRenameDialogFragment from activity depending on screen configuration.
 */

public interface OpenTagRenameDialogListener {
    public void openTagRenameDialog(int repositoryId, String repositoryTag);
}
