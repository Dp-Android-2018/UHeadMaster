package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 27/09/2017.
 */

public class Answer {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("question_id")
    private int questionId;

    @SerializedName("content")
    private String content;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;


    @SerializedName("user")
    private Questioner user;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Questioner getUser() {
        return user;
    }
}
