package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 15/08/2017.
 */

public class RegisterResponse {

    /*"status": 200,
    "token": "00iacxI4UKMLCdFOlRVryUkG4Dz17tVDiIwOVlQ2LzzBlAkcFN4WjjozrOIN",
    "id": 230,
    "name": "Test",
    "email": "test@test.test",
    "type": "student",
}*/
    @SerializedName("status")
    private int status;
    @SerializedName("token")
    private String token;
    @SerializedName("id")
    private int id;
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
}
