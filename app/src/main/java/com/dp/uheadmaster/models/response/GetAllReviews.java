package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.ReviewModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 13/09/2017.
 */

public class GetAllReviews {

    @SerializedName("status")
    private int statusCode;
    @SerializedName("message")
    private String message;

    @SerializedName("rates")
    private ArrayList<ReviewModel> rates;

    @SerializedName("next_page_url")
    private String nextPageUrl;

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }


    public ArrayList<ReviewModel> getRates() {
        return rates;
    }
}
