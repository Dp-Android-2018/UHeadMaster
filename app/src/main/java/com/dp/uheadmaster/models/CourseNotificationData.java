package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 02/01/2018.
 */

public class CourseNotificationData {

    @SerializedName("question")
    private int question;
    @SerializedName("announcement")
    private int announcement;

    public int getQuestion() {
        return question;
    }

    public int getAnnouncement() {
        return announcement;
    }
}
