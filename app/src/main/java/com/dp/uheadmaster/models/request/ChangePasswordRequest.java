package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 19/08/2017.
 */

public class ChangePasswordRequest {

    @SerializedName("old_password")
    private String oldPassword;
    @SerializedName("password")
    private String password;
    @SerializedName("password_confirmation")
    private String passwordConfirmation;

    public ChangePasswordRequest(String oldPassword, String password, String passwordConfirmation) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}
