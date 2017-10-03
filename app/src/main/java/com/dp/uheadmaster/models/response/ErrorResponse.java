package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class ErrorResponse {


    @SerializedName("status")
    private int statusCode;
    @SerializedName("message")
    private String message;

    public ErrorResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
