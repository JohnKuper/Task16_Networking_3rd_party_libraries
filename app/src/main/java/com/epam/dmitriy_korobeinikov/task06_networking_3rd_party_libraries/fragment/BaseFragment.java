package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 * Base fragment with common methods.
 */
public class BaseFragment extends Fragment {

    public ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }
}
