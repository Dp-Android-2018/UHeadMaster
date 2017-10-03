package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.Answer;
import com.dp.uheadmaster.models.Question;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 27/09/2017.
 */

public class AnswersResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("answers")
    private ArrayList<Answer> answerResponse;

    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public ArrayList<Answer> getAnswerResponse() {
        return answerResponse;
    }

    public String getMessage() {
        return message;
    }
}
