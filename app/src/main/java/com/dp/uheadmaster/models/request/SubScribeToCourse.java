package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 02/01/2018.
 */

public class SubScribeToCourse {
    @SerializedName("course_id")
    private int cid;

    @SerializedName("announcement")
    private boolean announcement;

    @SerializedName("question")
    private boolean question;

    public SubScribeToCourse(int cid, boolean announcement, boolean question) {
        this.cid = cid;
        this.announcement = announcement;
        this.question = question;
    }
}
