package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;

/**
 * Created by Dmitriy Korobeynikov on 1/13/2015.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

}
