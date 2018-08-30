package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 11/17/2017.
 */

public class CourseTargetRequestModel {

    @SerializedName("course_id")
    private int courseId;
    @SerializedName("target")
    String[] answers;

    public CourseTargetRequestModel(int courseId, String[] answers) {
        this.courseId = courseId;
        this.answers = answers;
    }

}
