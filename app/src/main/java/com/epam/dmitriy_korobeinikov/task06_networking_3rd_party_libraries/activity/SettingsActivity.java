package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.receiver.RepositoryBroadcastReceiver;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.AlarmManagerUtils;

/**
 * Created by Dmitriy Korobeynikov on 1/13/2015.
 * Used for setup settings in the application.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    public static final String PREF_REPO_NAME_KEY = "prefRepoName";
    public static final String PREF_OWNER_LOGIN_KEY = "prefOwnerLogin";
    public static final String PREF_CHECK_FREQUENCY_KEY = "prefCheckFrequency";
    private static boolean mIsPreviousValueNever;

    private Context mContext;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        addPreferencesFromResource(R.xml.settings);
        mIsPreviousValueNever = isPreviousValueNever();
        findPreference(PREF_CHECK_FREQUENCY_KEY).setOnPreferenceChangeListener(this);

    }

    private boolean isPreviousValueNever() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(PREF_CHECK_FREQUENCY_KEY, "0").equals("0");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String val = (String) newValue;
        String key = preference.getKey();
        switch (key) {
            case PREF_CHECK_FREQUENCY_KEY:
                if (!val.equals("0")) {
                    Log.d(LOG_TAG, "Selected value is: " + val);
                    preference.setSummary(getString(R.string.check_frequency_summary_in_minutes, val));
                    if (mIsPreviousValueNever) {
                        sendBroadcast(RepositoryBroadcastReceiver.getIncomingIntent());
                        mIsPreviousValueNever = false;
                    }
                } else if (val.equals("0")) {
                    Log.d(LOG_TAG, "Never value was selected");
                    mIsPreviousValueNever = true;
                    preference.setSummary(getString(R.string.check_frequency_never));
                    AlarmManagerUtils.cancelAlarmManager(mContext, RepositoryBroadcastReceiver.getPendingIntent(mContext));
                }
                break;
        }
        return true;
    }

    public static void setPreviousValueNever(boolean isPreviousValueNever) {
        mIsPreviousValueNever = isPreviousValueNever;
    }
}
