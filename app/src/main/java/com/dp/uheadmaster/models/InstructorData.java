package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 09/09/2017.
 */

public class InstructorData {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("headline")
    private String headline;

    @SerializedName("about")
    private String about;

    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHeadline() {
        return headline;
    }

    public String getAbout() {
        return about;
    }

    public String getImage() {
        return image;
    }
}
