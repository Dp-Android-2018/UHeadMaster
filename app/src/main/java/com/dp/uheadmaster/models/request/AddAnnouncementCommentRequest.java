package com.dp.uheadmaster.models.request;

/**
 * Created by DELL on 24/10/2017.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by لا اله الا الله on 23/10/2017.
 */
public class AddAnnouncementCommentRequest {

    @SerializedName("announcement_id")
    private int announcementId;
    @SerializedName("comment")
    private String comment;

    public AddAnnouncementCommentRequest(int announcementId, String comment) {
        this.announcementId = announcementId;
        this.comment = comment;
    }
}

