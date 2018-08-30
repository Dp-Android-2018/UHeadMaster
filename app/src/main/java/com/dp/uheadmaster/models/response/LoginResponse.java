package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class LoginResponse {

    /*"
    "status": 200,
    "token": "00iacxI4UKMLCdFOlRVryUkG4Dz17tVDiIwOVlQ2LzzBlAkcFN4WjjozrOIN",
    "id": 230,
    "name ": "Test",
    "email": "test@test.test",
    "mobile": "010659469580",
    “type”:”student”
*/
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
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
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("type")
    private String type;

    @SerializedName("country_key")
    private String countryKey;

    @SerializedName("has_password")
    private String hasPassword;

    @SerializedName("about")
    private String about;

    @SerializedName("image")
    private String image;


    @SerializedName("in_newsletter")
    private String isSubScribed;

    public int getConfirmed() {
        return confirmed;
    }

    public String getIsSubScribed() {
        return isSubScribed;
    }

    public String getCountryKey() {
        return countryKey;
    }

    public String getHasPassword() {
        return hasPassword;
    }

    public String getAbout() {
        return about;
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

    public String getMobile() {
        return mobile;
    }

    public String getType() {
        return type;
    }
}
