package com.dp.uheadmaster.models.response;

/**
 * Created by DELL on 24/10/2017.
 */

import com.dp.uheadmaster.models.Comment;
import com.dp.uheadmaster.models.ReviewModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by لا اله الا الله on 23/10/2017.
 */
public class GetAnnouncementCommentsResponse {

    @SerializedName("status")
    private int statusCode;
    @SerializedName("message")
    private String message;

    @SerializedName("comments")
    private ArrayList<Comment> comments;

    @SerializedName("next_page_url")
    private String nextPageUrl;


    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }
}

