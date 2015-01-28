package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.R;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthBody;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthResponse;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit.GitHubAuthorizationRequest;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.spiceservice.BaseGitHubSpiceService;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth.AccountGeneral.*;

/**
 * Created by Dmitriy Korobeynikov on 1/22/2015.
 * Calls by RepositoriesAuthenticatorService when user wanted to add new account.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private static final String LOG_TAG = AuthenticatorActivity.class.getSimpleName();

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public final static String PARAM_USER_PASS = "USER_PASS";
    private final static String PREFIX_BASIC_AUTHORIZATION = "basic ";
    private final static String PREFIX_TOKEN_AUTHORIZATION = "token ";

    private AccountManager mAccountManager;
    private SpiceManager mSpiceManager = new SpiceManager(BaseGitHubSpiceService.class);
    private String mAccountType;
    private String mAuthTokenType;
    private String mUserName;
    private String mUserPass;

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
            mAuthTokenType = AUTHTOKEN_TYPE_FULL_ACCESS;

        if (accountName != null) {
            ((TextView) findViewById(R.id.accountName)).setText(accountName);
        }

        findViewById(R.id.submit_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAuthorization();
            }
        });
        findViewById(R.id.cancel_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(AuthenticatorActivity.this);
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    private void submitAuthorization() {
        mUserName = ((TextView) findViewById(R.id.accountName)).getText().toString();
        mUserPass = ((TextView) findViewById(R.id.accountPassword)).getText().toString();

        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Started authenticating");

        AuthBody authBody = fillAuthBody(CLIENT_SECRET, ACCOUNT_SCOPES, ACCOUNT_NOTE);

        String authHeader = PREFIX_BASIC_AUTHORIZATION + getEncodedUserDetails(mUserName, mUserPass);

        GitHubAuthorizationRequest authorizationRequest = new GitHubAuthorizationRequest(CLIENT_ID, authBody, authHeader);
        mSpiceManager.execute(authorizationRequest, new AuthResponseListener());
    }

    private class AuthResponseListener implements RequestListener<AuthResponse> {
        @Override
        public void onRequestFailure(SpiceException e) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> onRequestFailure ", e);
        }

        @Override
        public void onRequestSuccess(AuthResponse authResponse) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> onRequestSuccess");

            String authToken = authResponse.getToken();
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> token - " + authToken);

            Bundle data = new Bundle();
            data.putString(AccountManager.KEY_ACCOUNT_NAME, mUserName);
            data.putString(PARAM_USER_PASS, mUserPass);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
            data.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            final Intent res = new Intent();
            res.putExtras(data);

            finishLogin(res);
        }

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

    private AuthBody fillAuthBody(String clientSecret, String[] scopes, String note) {
        AuthBody authBody = new AuthBody();
        authBody.setClientSecret(clientSecret);
        authBody.setScopes(scopes);
        authBody.setNote(note);
        return authBody;
    }
}
