package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 11/11/2017.
 */

public class CreateCourseResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    @SerializedName("course_id")
    private int courseId;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getCourseId() {
        return courseId;
    }
}
