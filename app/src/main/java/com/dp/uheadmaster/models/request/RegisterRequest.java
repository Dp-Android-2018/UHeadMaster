package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class RegisterRequest {
    /*name
email
password
*/

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("device_token")
    private String deviceToken;

    public RegisterRequest(String name, String email, String password, String deviceToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.deviceToken = deviceToken;
    }
}
