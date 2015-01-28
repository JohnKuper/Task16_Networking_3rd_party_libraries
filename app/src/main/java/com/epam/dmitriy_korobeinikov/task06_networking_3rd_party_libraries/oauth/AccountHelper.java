package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.oauth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import java.io.IOException;

/**
 * Created by Dmitriy Korobeynikov on 28.01.2015.
 */
public class AccountHelper {

    private static final String LOG_TAG = AccountHelper.class.getSimpleName();
    private AccountManager mAccountManager;

    public AccountHelper(Context context) {
        mAccountManager = AccountManager.get(context);
    }

    public String getAccountToken(String accountName) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> getAuthToken");
        Account account = getAccountByName(accountName);
        try {
            return mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
            Log.e(RepositoriesApplication.APP_NAME, LOG_TAG + "> Error during getAuthToken", e);
        }
        return null;
    }

    public Account getAccountByName(String accountName) {
        Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> getAccountByName");
        Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equals(accountName)) return account;
        }
        throw new IllegalArgumentException("Unknown account: " + accountName);
    }
}
