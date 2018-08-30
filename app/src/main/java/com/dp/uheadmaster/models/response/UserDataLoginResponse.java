package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 16/08/2017.
 */

public class UserDataLoginResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("token")
    private String token;

    @SerializedName("id")
    private int id;

    @SerializedName("confirmed")
    private int confirmed;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("type")
    private String type;

    @SerializedName("message")
    private String message;

    @SerializedName("image")
    private String image;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("country_key")
    private String countryKey;


    @SerializedName("about")
    private String about;

    @SerializedName("in_newsletter")
    private String isSubScribed;

    public String getIsSubScribed() {
        return isSubScribed;
    }

    public String getCountryKey() {
        return countryKey;
    }

    public String getAbout() {
        return about;
    }

    public String getMobile() {
        return mobile;
    }

    public String getImage() {
        return image;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public int getConfirmed() {
        return confirmed;
    }
}
