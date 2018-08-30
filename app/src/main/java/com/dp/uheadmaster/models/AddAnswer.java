package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 25/10/2017.
 */

public class AddAnswer {


    @SerializedName("question_id")
    private int questionId;

    @SerializedName("content")
    private String content;

    public AddAnswer(int questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }
}
