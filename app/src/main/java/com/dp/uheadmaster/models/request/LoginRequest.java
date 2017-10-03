package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class LoginRequest {
    /*name
email
password
*/
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("device_token")
    private String deviceToken;

    public LoginRequest( String email, String password, String deviceToken) {
        this.email = email;
        this.password = password;
        this.deviceToken = deviceToken;
    }
}
