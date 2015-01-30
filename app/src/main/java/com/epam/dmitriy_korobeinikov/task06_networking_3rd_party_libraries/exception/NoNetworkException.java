package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception;

/**
 * Created by Dmitriy Korobeynikov on 1/30/2015.
 */
public class NoNetworkException extends Exception {

    public NoNetworkException() {
        super("Network is not available");
    }
}
