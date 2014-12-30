package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener;

import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.RepoCursorAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;

/**
 * Created by Dmitriy_Korobeinikov on 12/29/2014.
 */
public class CursorLoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private RepoCursorAdapter mRepoCursorAdapter;
    private String mKeyword;

    public CursorLoaderListener(Context context, RepoCursorAdapter repoCursorAdapter, String selection) {
        mContext = context;
        mRepoCursorAdapter = repoCursorAdapter;
        mKeyword = selection;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String selection = RepositoryContent.DESCRIPTION + " LIKE '%" + mKeyword + "%'";
        return new CursorLoader(mContext, RepositoryContent.REPOSITORIES_URI, null, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRepoCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mRepoCursorAdapter.changeCursor(null);
    }
}
