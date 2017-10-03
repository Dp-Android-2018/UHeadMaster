package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 10/2/2017.
 */

public class AddCourseQuestionRequest {

    @SerializedName("course_id")
    private int courseId;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

    public AddCourseQuestionRequest(int courseId, String title, String content) {
        this.courseId = courseId;
        this.title = title;
        this.content = content;
    }

    /* 	course_id
    	title
  	content
*/
}
