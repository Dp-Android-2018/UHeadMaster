package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 04/10/2017.
 */

public class CourseContentModel {

    @SerializedName("status")
    private int status;

    @SerializedName("sections")
    private ArrayList<Section>sections;


    public int getStatus() {
        return status;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }
}
