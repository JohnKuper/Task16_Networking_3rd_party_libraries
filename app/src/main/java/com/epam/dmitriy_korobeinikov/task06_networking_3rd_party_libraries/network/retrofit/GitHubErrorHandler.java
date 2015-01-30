package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import android.content.Context;
import android.util.Log;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception.NoNetworkException;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception.UnauthorizedException;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesApplication;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dmitriy Korobeynikov on 1/30/2015.
 */
public class GitHubErrorHandler implements ErrorHandler {
    private static final String LOG_TAG = GitHubErrorHandler.class.getSimpleName();

    @Override
    public Throwable handleError(RetrofitError cause) {
        if (cause.getKind() == RetrofitError.Kind.NETWORK) {
            Log.d(RepositoriesApplication.APP_NAME, LOG_TAG + "> Cannot connect to " + cause.getUrl());
            return new NoNetworkException();
        }

        Response response = cause.getResponse();
        if (response != null) {
            int status = response.getStatus();
            if (status == 401) {
                Log.w(RepositoriesApplication.APP_NAME, LOG_TAG + "> Access in not authorized " + cause.getUrl());
                return new UnauthorizedException("Access in not authorized " + cause.getUrl(), cause);
            } else if (status >= 300) {
                Log.w(RepositoriesApplication.APP_NAME, LOG_TAG + "> Error " + String.valueOf(status) + " while accessing " + cause.getUrl());
                return cause;
            }
        }
        return cause;
    }
}
