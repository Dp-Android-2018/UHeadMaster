package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 10/10/2017.
 */

public class AnnouncementData {


    @SerializedName("id")
    private int id;

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("updated_at")
    private String upatedAt;

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUpatedAt() {
        return upatedAt;
    }
}
