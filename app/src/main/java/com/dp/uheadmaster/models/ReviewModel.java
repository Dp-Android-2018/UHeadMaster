package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 13/09/2017.
 */

public class ReviewModel {

    @SerializedName("id")
    private int id;

    @SerializedName("user")
    private String user;

    @SerializedName("rate")
    private String rate;

    @SerializedName("user_image")
    private String userImage;

    @SerializedName("comment")
    private String comment;

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public String getUserImage() {
        return userImage;
    }
}
