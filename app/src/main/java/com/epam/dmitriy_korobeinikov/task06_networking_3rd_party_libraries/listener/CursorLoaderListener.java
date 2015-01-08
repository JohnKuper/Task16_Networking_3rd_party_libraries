package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.adapter.RepoCursorAdapter;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;

/**
 * Created by Dmitriy Korobeynikov on 12/29/2014.
 * This class is used for handling cursor loader's callbacks
 * and swap obtained cursor to RepoCursorAdapter.
 */
public class CursorLoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private RepoCursorAdapter mRepoCursorAdapter;
    private String mKeyword;

    public CursorLoaderListener(Context context, RepoCursorAdapter repoCursorAdapter, String keyword) {
        mContext = context;
        mRepoCursorAdapter = repoCursorAdapter;
        mKeyword = keyword;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = {"%" + mKeyword + "%", "%" + mKeyword + "%"};
        String selection = RepositoryContent.DESCRIPTION + " LIKE ?" + " OR " + RepositoryContent.NAME + " LIKE ?";
        return new CursorLoader(mContext, RepositoryContent.REPOSITORIES_URI, null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRepoCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mRepoCursorAdapter.swapCursor(null);
    }
}
