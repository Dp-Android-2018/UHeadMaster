package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 04/10/2017.
 */

public class Section {

    @SerializedName("id")
     private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("lectures")
    private ArrayList<CourseData> courseDatas;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<CourseData> getCourseDatas() {
        return courseDatas;
    }
}
