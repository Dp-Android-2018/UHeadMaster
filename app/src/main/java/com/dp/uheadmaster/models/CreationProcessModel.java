package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 10/24/2017.
 */

public class CreationProcessModel {
    /*
    * {
             "id": 1,
            "process": "Step One",
            "description": "This is the description",
            "created_at": null,
            "updated_at": null
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
