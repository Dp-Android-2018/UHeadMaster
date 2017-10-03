package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.Question;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 27/09/2017.
 */

public class QuestionsResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("questions")
    private ArrayList<Question> questions;

    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public String getMessage() {
        return message;
    }
}
