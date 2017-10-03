package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DELL on 27/09/2017.
 */

public class Questioner implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("imageLink")
    private String imageLink;

    @SerializedName("thumbLink")
    private String thumbLink;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getThumbLink() {
        return thumbLink;
    }
}
