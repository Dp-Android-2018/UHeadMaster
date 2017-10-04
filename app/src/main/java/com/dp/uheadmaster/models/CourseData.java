package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 04/10/2017.
 */

public class CourseData {


    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String type;

    @SerializedName("contents")
    private ArrayList<Content>contents;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Content> getContents() {
        return contents;
    }
}
