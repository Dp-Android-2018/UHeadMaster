package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.CourseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 05/09/2017.
 */

public class SearchCoursesResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("next_page_url")
    private String nextPageUrl;

    @SerializedName("courses")
    private ArrayList<CourseModel> courses;

    public int getStatus() {
        return status;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public ArrayList<CourseModel> getCourses() {
        return courses;
    }
}
