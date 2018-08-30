package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.CreationProcessModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 10/24/2017.
 */

public class CreationProcessResponse {
/*
*
        "status": 200,
    "creation_process": [
        {
            "id": 1,
            "process": "Step One",
            "description": "This is the description",
            "created_at": null,
            "updated_at": null
        },
        {
            "id": 3,
            "process": "One thing more to test",
            "description": "<p>some of this description thing</p>",
            "created_at": "2017-10-22 20:28:29",
            "updated_at": "2017-10-22 20:28:29"
        }
    ]

*/

    @SerializedName("status")
    private int status;
    @SerializedName("creation_process")
    List<CreationProcessModel> textModels;

    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public List<CreationProcessModel> getTextModels() {
        return textModels;
    }

    public String getMessage() {
        return message;
    }
}
