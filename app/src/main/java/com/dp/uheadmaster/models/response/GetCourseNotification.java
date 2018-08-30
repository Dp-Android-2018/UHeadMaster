package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.CourseNotificationData;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 02/01/2018.
 */

public class GetCourseNotification {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private CourseNotificationData data;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public CourseNotificationData getData() {
        return data;
    }
}
