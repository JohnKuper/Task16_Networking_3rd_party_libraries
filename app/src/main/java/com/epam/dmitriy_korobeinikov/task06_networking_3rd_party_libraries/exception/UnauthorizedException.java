package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.exception;

/**
 * Created by Dmitriy Korobeynikov on 1/30/2015.
 */
public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String detailMessage) {

        super(detailMessage);
    }

    public UnauthorizedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
