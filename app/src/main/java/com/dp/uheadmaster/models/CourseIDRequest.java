package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 8/27/2017.
 */

public class CourseIDRequest {

    @SerializedName("course_id")
    private int courseID;

    public CourseIDRequest(int courseID) {
        this.courseID = courseID;
    }
}
