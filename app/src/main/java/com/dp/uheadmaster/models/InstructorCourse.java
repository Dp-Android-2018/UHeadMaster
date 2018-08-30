package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DELL on 24/10/2017.
 */

public class InstructorCourse implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("instructor_id")
    private int instructorId;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("sub_category_id")
    private int subCategoryId;

    @SerializedName("title")
    private String title;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("prerequisites")
    private ArrayList<String> prerequisites;

    @SerializedName("target")
    private ArrayList<String> target;

    @SerializedName("to_learn")
    private ArrayList<String> to_learn;

    @SerializedName("price")
    private String price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("video")
    private String video;

    @SerializedName("course_level")
    private String courseLevel;

    @SerializedName("rate")
    private String rate;

    @SerializedName("no_of_students")
    private int numOfStudents;

    @SerializedName("no_of_videos")
    private int numOfvideos;

    @SerializedName("no_of_lectures")
    private int numOfLectures;

    @SerializedName("course_duration")
    private int courseDuration;

    @SerializedName("approved")
    private int approved;

    @SerializedName("status")
    private String status;

    @SerializedName("slug")
    private String slug;

    @SerializedName("lang")
    private String lang;


    public int getNumOfvideos() {
        return numOfvideos;
    }

    public int getId() {
        return id;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    public ArrayList<String> getTarget() {
        return target;
    }

    public ArrayList<String> getTo_learn() {
        return to_learn;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public String getRate() {
        return rate;
    }

    public int getNumOfStudents() {
        return numOfStudents;
    }

    public int getNumOfLectures() {
        return numOfLectures;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public int getApproved() {
        return approved;
    }

    public String getStatus() {
        return status;
    }

    public String getSlug() {
        return slug;
    }

    public String getLang() {
        return lang;
    }
}
