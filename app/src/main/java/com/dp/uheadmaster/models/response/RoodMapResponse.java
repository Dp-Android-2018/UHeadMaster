package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.RoodMapModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 10/24/2017.
 */

public class RoodMapResponse {
/*
*
    "status": 200,
    "roadmap": [
        {
            "id": 3,
            "step": "First Step",
            "explanation": "Something here",
            "created_at": "2017-10-22 18:43:10",
            "updated_at": "2017-10-22 18:43:10"
        },
        {
            "id": 5,
            "step": "One More Step",
            "explanation": "Something more here",
            "created_at": "2017-10-22 18:43:39",
            "updated_at": "2017-10-22 18:43:39"
        }...
    ]
*/

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("roadmap")
    List<RoodMapModel> textModels;

    public int getStatus() {
        return status;
    }

    public List<RoodMapModel> getTextModels() {
        return textModels;
    }

    public String getMessage() {
        return message;
    }
}
