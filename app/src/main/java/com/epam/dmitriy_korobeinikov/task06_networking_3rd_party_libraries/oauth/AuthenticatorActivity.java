package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthBody;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthResponse;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.DBCacheSpiceService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.GitHub;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

/**
 * Created by Dmitriy Korobeynikov on 1/22/2015.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private static final String LOG_TAG = AuthenticatorActivity.class.getSimpleName();
    private static final String CLIENT_ID = "57d08abea730de89c508";
    private static final String CLIENT_SECRET = "425df65f5d7f34bad1287dbc52cede6a3b082cad";

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        if (accountName != null) {
            ((TextView) findViewById(R.id.accountName)).setText(accountName);
        }

        findViewById(R.id.submit_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    public void submit() {

        final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.accountPassword)).getText().toString();

        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {

                Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Started authenticating");

                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    authtoken = getAuthToken(userName, userPass);
                    Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> token - " + authtoken);

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                    data.putString(PARAM_USER_PASS, userPass);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);

                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getEncodedUserDetails(String userName, String password) {
        String total = userName + ":" + password;
        String encoded = Base64.encodeToString(total.getBytes(), Base64.DEFAULT);
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> encoded details - " + encoded);
        return encoded;
    }

    private String getAuthToken(String userName, String password) {
        AuthBody authBody = new AuthBody();
        authBody.setClientSecret(CLIENT_SECRET);

        String[] scopes = {"public_repo"};
        authBody.setScopes(scopes);

        authBody.setNote("admin script");

        String header = "Basic " + getEncodedUserDetails(userName, password);

        GitHub gitHub = DBCacheSpiceService.getGitHubRestAdapter();
        AuthResponse authResponse = gitHub.authorization(CLIENT_ID, authBody, header);

        return authResponse.getToken();
    }

}
