package com.dp.uheadmaster.models.error;

/**
 * Created by DELL on 16/08/2017.
 */

public class APIError {
    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}

