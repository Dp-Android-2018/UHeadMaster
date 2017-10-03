package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.InstructorData;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 09/09/2017.
 */

public class InstructorResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    @SerializedName("instructor")
    private InstructorData instructor;

    public int getStatus() {
        return status;
    }

    public InstructorData getInstructor() {
        return instructor;
    }
}
