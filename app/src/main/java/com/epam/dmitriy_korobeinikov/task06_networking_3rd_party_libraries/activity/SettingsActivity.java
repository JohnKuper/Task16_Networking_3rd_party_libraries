package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.BaseContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;

/**
 * Created by Dmitriy Korobeynikov on 1/13/2015.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String PREF_REPO_NAME_KEY = "prefRepoName";
    public static final String PREF_OWNER_LOGIN_KEY = "prefOwnerLogin";
    public static final String PREF_CHECK_FREQUENCY_KEY = "prefCheckFrequency";
    private static boolean mIsPreviousValueNever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        mIsPreviousValueNever = isPreviousValueNever();
        findPreference(PREF_CHECK_FREQUENCY_KEY).setOnPreferenceChangeListener(this);
    }

    private boolean isPreviousValueNever() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(PREF_CHECK_FREQUENCY_KEY, "0").equals("0");
    }

    private void sendBroadCastForStartCheckService() {
        Intent intent = new Intent();
        intent.setAction(RepositoryBroadcastReceiver.RECEIVER_ACTION);
        sendBroadcast(intent);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String val = (String) newValue;
        String key = preference.getKey();
        switch (key) {
            case PREF_CHECK_FREQUENCY_KEY:
                if (!val.equals("0")) {
                    Log.d(BaseContent.LOG_TAG_TASK_06, "Selected value is: " + val);
                    preference.setSummary(val + " minute(s)");
                    if (mIsPreviousValueNever) {
                        sendBroadCastForStartCheckService();
                        mIsPreviousValueNever = false;
                    }
                } else if (val.equals("0")) {
                    Log.d(BaseContent.LOG_TAG_TASK_06, "Never value was selected");
                    mIsPreviousValueNever = true;
                    preference.setSummary("Never");
                    RepositoryBroadcastReceiver.cancelAlarmManager(getApplicationContext());
                }
                break;
        }
        return true;
    }

    public static void setPreviousValueNever(boolean isPreviousValueNever) {
        mIsPreviousValueNever = isPreviousValueNever;
    }
}
