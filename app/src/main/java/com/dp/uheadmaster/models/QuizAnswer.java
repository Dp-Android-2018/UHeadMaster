package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DELL on 04/10/2017.
 */

public class QuizAnswer implements Serializable {

    @SerializedName("checked")
    private boolean checked;

    @SerializedName("answer")
    private String answer;


    public boolean isChecked() {
        return checked;
    }

    public String getAnswer() {
        return answer;
    }
}
