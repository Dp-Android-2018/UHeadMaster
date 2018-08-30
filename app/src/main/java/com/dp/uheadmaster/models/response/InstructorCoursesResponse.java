package com.dp.uheadmaster.models.response;

/**
 * Created by DELL on 24/10/2017.
 */

import com.dp.uheadmaster.models.Comment;
import com.dp.uheadmaster.models.InstructorCourse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by لا اله الا الله on 23/10/2017.
 */
public class InstructorCoursesResponse {

    @SerializedName("status")
    private int statusCode;
    @SerializedName("message")
    private String message;

    @SerializedName("courses")
    private ArrayList<InstructorCourse> courses;

    @SerializedName("next_page_url")
    private String nextPageUrl;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<InstructorCourse> getCourses() {
        return courses;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }
}
