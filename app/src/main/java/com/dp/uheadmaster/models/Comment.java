package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 24/10/2017.
 */

public class Comment {

    @SerializedName("id")
    private int id;
    @SerializedName("announcement_id")
    private int announcementId;

    @SerializedName("created_at")
    private String createdAt;


    @SerializedName("user")
    private Questioner user;


    public String getCreatedAt() {
        return createdAt;
    }

    @SerializedName("comment")
    private String comment;

    public int getId() {
        return id;
    }

    public int getAnnouncementId() {
        return announcementId;
    }

    public Questioner getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }
}
