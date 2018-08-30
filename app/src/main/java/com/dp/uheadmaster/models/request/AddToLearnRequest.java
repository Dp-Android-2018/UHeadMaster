package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 07/11/2017.
 */

public class AddToLearnRequest {


    @SerializedName("course_id")
    private int courseId;
    @SerializedName("to_learn")
    private ArrayList<String> toLearn;

    public AddToLearnRequest(int courseId, ArrayList<String> toLearn) {
        this.courseId = courseId;
        this.toLearn = toLearn;
    }
}
