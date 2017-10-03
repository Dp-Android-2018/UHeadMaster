package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class ResetPasswordRequest {
    /*name
email
password
*/
    @SerializedName("email")
    private String email;


    public ResetPasswordRequest(String email) {
        this.email = email;
    }
}
