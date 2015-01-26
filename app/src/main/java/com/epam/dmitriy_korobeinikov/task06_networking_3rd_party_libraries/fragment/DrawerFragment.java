package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;

/**
 * Created by Dmitriy Korobeynikov on 1/23/2015.
 */
public class DrawerFragment extends Fragment {

    private ListView mCategoryList;
    private String[] mDrawerValues;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drawer, container, false);

        mDrawerValues = getResources().getStringArray(R.array.checkFrequency);
        mCategoryList = (ListView) v.findViewById(R.id.list_drawer_menu);

        mCategoryList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDrawerValues));

        return v;
    }


}
