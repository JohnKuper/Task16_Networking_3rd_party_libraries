package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Dmitriy Korobeynikov on 1/22/2015.
 */
public class RepositoriesAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        GitHubAuthenticator authenticator = new GitHubAuthenticator(this);
        return authenticator.getIBinder();
    }
}
