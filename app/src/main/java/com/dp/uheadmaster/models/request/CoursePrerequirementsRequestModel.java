package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 11/17/2017.
 */

public class CoursePrerequirementsRequestModel {

    @SerializedName("course_id")
    private int courseId;
    @SerializedName("prerequisites")
    String[] answers;

    public CoursePrerequirementsRequestModel(int courseId, String[] answers) {
        this.courseId = courseId;
        this.answers = answers;
    }


}
