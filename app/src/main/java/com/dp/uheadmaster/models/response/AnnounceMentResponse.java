package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.Announcement;
import com.dp.uheadmaster.models.Answer;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 10/10/2017.
 */

public class AnnounceMentResponse {


    @SerializedName("status")
    private int status;

    @SerializedName("announcements")
    private Announcement announcements;

    public int getStatus() {
        return status;
    }

    public Announcement getAnnouncements() {
        return announcements;
    }
}
