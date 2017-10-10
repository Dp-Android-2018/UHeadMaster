package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 10/10/2017.
 */

public class Announcement {

    @SerializedName("data")
    private ArrayList<AnnouncementData>announcementData;

    @SerializedName("next_page_url")
    private String nextPage;

    public ArrayList<AnnouncementData> getAnnouncementData() {
        return announcementData;
    }

    public String getNextPage() {
        return nextPage;
    }
}
