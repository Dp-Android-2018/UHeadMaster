package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class UpdateEmailRequest {

    @SerializedName("email")
    private String email;

    public UpdateEmailRequest(String email) {
        this.email = email;
    }
}
