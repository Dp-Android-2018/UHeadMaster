package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 11/1/2017.
 */

public class AutomaticMessageReqest {

    @SerializedName("course_id")
    private int courseID;
    @SerializedName("welcome_message")
    private String wlecomeMessage;
    @SerializedName("congrats_message")
    private String congratsMessage;

    public AutomaticMessageReqest(int courseID, String wlecomeMessage, String congratsMessage) {
        this.courseID = courseID;
        this.wlecomeMessage = wlecomeMessage;
        this.congratsMessage = congratsMessage;
    }
}
