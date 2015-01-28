package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Dmitriy Korobeynikov on 28.01.2015.
 */
public class GithubSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static GithubSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new GithubSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
