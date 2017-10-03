package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 20/08/2017.
 */

public class UpdateProfile {

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("country_key")
    private String country_key;

    @SerializedName("about")
    private String about;

    public UpdateProfile(String name, String mobile, String country_key, String about) {
        this.name = name;
        this.mobile = mobile;
        this.country_key = country_key;
        this.about = about;
    }
}
