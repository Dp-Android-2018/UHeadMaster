package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 10/22/2017.
 */

public class LastWatchedRequest {

    @SerializedName("video")
    private String videoTitle;

    public LastWatchedRequest(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
