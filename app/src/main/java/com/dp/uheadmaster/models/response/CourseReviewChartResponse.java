package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class CourseReviewChartResponse {

    /*"status": "200",
  "content_rate": "3.50",
  "instructor_rate": "5.00",
  "provider_rate": "3.50"
*/


    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("content_rate")
    private String contentRate;
    @SerializedName("instructor_rate")
    private String instructorRate;
    @SerializedName("provider_rate")
    private String providerRate;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getContentRate() {
        return contentRate;
    }

    public String getInstructorRate() {
        return instructorRate;
    }

    public String getProviderRate() {
        return providerRate;
    }
}
