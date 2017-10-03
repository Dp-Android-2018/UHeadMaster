package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class ResetPasswordResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;


    public String getMessage() {
        return message;
    }
    public int getStatus() {
        return status;
    }


}
