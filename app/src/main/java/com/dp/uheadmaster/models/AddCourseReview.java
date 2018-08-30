package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 20/01/2018.
 */

public class AddCourseReview {

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("content_rate")
    private double contentRate;

    @SerializedName("instructor_rate")
    private double instructorRate;

    @SerializedName("provider_rate")
    private double providerRate;

    @SerializedName("comment")
    private String comment;

    public AddCourseReview( int courseId, double contentRate, double instructorRate, double providerRate, String comment) {
        this.courseId = courseId;
        this.contentRate = contentRate;
        this.instructorRate = instructorRate;
        this.providerRate = providerRate;
        this.comment = comment;
    }
}
