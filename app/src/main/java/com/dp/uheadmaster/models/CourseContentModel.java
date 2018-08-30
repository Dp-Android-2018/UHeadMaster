package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 04/10/2017.
 */

public class CourseContentModel {

    @SerializedName("status")
    private int status;


    @SerializedName("last-watched")
    private String lastWatched;

    @SerializedName("added_review")
    private boolean addedReview;

    @SerializedName("sections")
    private ArrayList<Section>sections;

    @SerializedName("message")
    private String message ;

    public String getMessage() {
        return message;
    }

    public String getLastWatched() {
        return lastWatched;
    }

    public int getStatus() {
        return status;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public boolean isAddedReview() {
        return addedReview;
    }
}
