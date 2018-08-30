package com.dp.uheadmaster.models.request;

/**
 * Created by DELL on 24/10/2017.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by لا اله الا الله on 23/10/2017.
 */
public class AddAnnouncementRequest {

    @SerializedName("course_id")
    private int courseId;
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;

    @SerializedName("end_date")
    private String endDate;


    public AddAnnouncementRequest(String title, int courseId, String content,String date) {
        this.title = title;
        this.courseId = courseId;
        this.content = content;
        this.endDate=date;
    }
}

