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
    private float rate;

    @SerializedName("comment")
    private String comment;

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public float getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }
}
