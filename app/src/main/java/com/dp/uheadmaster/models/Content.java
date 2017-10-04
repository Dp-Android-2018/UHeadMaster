package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 04/10/2017.
 */

public class Content {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private String content;

    @SerializedName("type")
    private String type;


    @SerializedName("answers")
    private ArrayList<QuizAnswer> answers;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public ArrayList<QuizAnswer> getAnswers() {
        return answers;
    }
}
