package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 11/11/2017.
 */

public class CreateCourseRequest {

    @SerializedName("title")
    private String title;
    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("lang")
    private String lang;

    @SerializedName("category_id")
    private int categoryid;


    @SerializedName("sub_category_id")
    private int subcategoryId;

    @SerializedName("course_level")
    private String courseLevel;


    @SerializedName("description")
    private String description;

    public CreateCourseRequest(String title, String subtitle, String lang, int categoryid, int subcategoryId, String courseLevel,String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.lang = lang;
        this.categoryid = categoryid;
        this.subcategoryId = subcategoryId;
        this.courseLevel = courseLevel;
        this.description=description;
    }
}
