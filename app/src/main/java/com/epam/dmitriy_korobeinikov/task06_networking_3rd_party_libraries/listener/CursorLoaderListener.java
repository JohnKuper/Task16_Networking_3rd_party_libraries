package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.listener;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

/**
 * Created by Dmitriy Korobeynikov on 12/29/2014.
 * This class is used for handling cursor loader's callbacks
 * and swap obtained cursor to RepoCursorAdapter.
 */
public class CursorLoaderListener<T extends CursorAdapter> implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private Uri mUri;
    private T mCursorAdapter;
    private String mSelection;
    private String[] mSelectionArgs;

    public CursorLoaderListener(Context context, Uri uri, T cursorAdapter, String selection, String[] selectionArgs) {
        mContext = context;
        mUri = uri;
        mCursorAdapter = cursorAdapter;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, mUri, null, mSelection, mSelectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCursorAdapter.swapCursor(null);
    }
}
