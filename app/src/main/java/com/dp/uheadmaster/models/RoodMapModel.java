package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 10/24/2017.
 */

public class RoodMapModel {
    /*
    * {
            "id": 5,
            "step": "One More Step",
            "explanation": "Something more here",
            "created_at": "2017-10-22 18:43:39",
            "updated_at": "2017-10-22 18:43:39"
        }*/

    @SerializedName("id")
    private int id;
    @SerializedName("step")
    private String stepTitle;
    @SerializedName("explanation")
    private String stepDec;
    @SerializedName("created_at")
    private String createAt;
    @SerializedName("updated_at")
    private String updateAt;

    public int getId() {
        return id;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public String getStepDec() {
        return stepDec;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }
}
