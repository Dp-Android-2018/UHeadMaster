package com.dp.uheadmaster.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DELL on 27/09/2017.
 */

public class Question implements Serializable{

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("content")
    private String content;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("answer_id")
    private int answerId;

    @SerializedName("title")
    private String title;

    @SerializedName("answers_count")
    private int answersCount;

    @SerializedName("user")
    private Questioner user;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getCourseId() {
        return courseId;
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

    public int getAnswerId() {
        return answerId;
    }

    public Questioner getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public int getAnswersCount() {
        return answersCount;
    }
}
