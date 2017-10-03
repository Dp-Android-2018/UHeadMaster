package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 10/2/2017.
 */

public class AddQuestionAnswerRequest {
    /* 	question_id
 	content
*/

    @SerializedName("question_id")
    private int questionId;
    @SerializedName("content")
    private String content;

    public AddQuestionAnswerRequest(int questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }
}
