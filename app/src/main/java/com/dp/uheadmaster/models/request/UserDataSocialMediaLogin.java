package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 16/08/2017.
 */

public class UserDataSocialMediaLogin {

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("device_token")
    private String deviceToken;

    public UserDataSocialMediaLogin(String identifier, String email, String name,String deviceToken) {
        this.identifier = identifier;
        this.email = email;
        this.name = name;
        this.deviceToken=deviceToken;
    }
}
