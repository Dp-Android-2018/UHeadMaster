package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class UpdatePrivacyRequest {

    @SerializedName("private")
    private boolean email;

    public UpdatePrivacyRequest(boolean email) {
        this.email = email;
    }
}
