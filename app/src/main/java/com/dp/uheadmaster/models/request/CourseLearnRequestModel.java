package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 11/17/2017.
 */

public class CourseLearnRequestModel {

    @SerializedName("course_id")
    private int courseId;
    @SerializedName("to_learn")
    String[] answers;

    public CourseLearnRequestModel(int courseId, String[] answers) {
        this.courseId = courseId;
        this.answers = answers;
    }
}
