package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.ReviewModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 31/10/2017.
 */

public class InstructorReviewsResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("rates")
    private ArrayList<ReviewModel> rates;

    @SerializedName("next_page_url")
    private String nextPageUrl;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ReviewModel> getRates() {
        return rates;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }
}
