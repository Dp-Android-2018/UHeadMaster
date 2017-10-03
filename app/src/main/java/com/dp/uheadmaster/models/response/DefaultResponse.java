package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 8/27/2017.
 */

public class DefaultResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
/*"status": 200,
  "message": "added"
*/