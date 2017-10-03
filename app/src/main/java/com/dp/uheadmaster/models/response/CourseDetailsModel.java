package com.dp.uheadmaster.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 8/27/2017.
 */

public class CourseDetailsModel {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String titile;
    @SerializedName("subtitle")
    private String subTitle;
    @SerializedName("prerequisites")
    private List<String> preRequisites;
    @SerializedName("price")
    private String price;
    @SerializedName("old_price")
    private String oldProice;
    @SerializedName("currency")
    private String currency;
    @SerializedName("image")
    private String imagePath;
    @SerializedName("video")
    private String vodeoPath;
    @SerializedName("description")
    private String description;
    @SerializedName("course_level")
    private String courseLevel;
    @SerializedName("no_of_students")
    private String noOfStudents;
    @SerializedName("no_of_lectures")
    private String noOfLectures;
    @SerializedName("course_duration")
    private String courseDuration;
    @SerializedName("rate")
    private String rate;
    @SerializedName("in_wishlist")
    private int isWishCourse;
    @SerializedName("in_cartlist")
    private int isInCart;
    @SerializedName("lang")
    private String langauge;

    @SerializedName("instructor_id")
    private int instructor_id;

    public int getInstructor_id() {
        return instructor_id;
    }

    public int getIsInCart() {
        return isInCart;
    }

    public String getLangauge() {
        return langauge;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getTitile() {
        return titile;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public List<String> getPreRequisites() {
        return preRequisites;
    }

    public String getPrice() {
        return price;
    }

    public String getOldProice() {
        return oldProice;
    }

    public String getCurrency() {
        return currency;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVodeoPath() {
        return vodeoPath;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public String getNoOfStudents() {
        return noOfStudents;
    }

    public String getNoOfLectures() {
        return noOfLectures;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public String getRate() {
        return rate;
    }

    public int getIsWishCourse() {
        return isWishCourse;
    }
}

    /*success	{
  "status": 200,
  "id": 1,
  "title": "Quae hic repellat id ullam.",
  "subtitle": "Dolor optio aliquid voluptatem accusantium.",
  "prerequisites": [
    "Something",
    "something else",
    "one more thing"
  ],
  "price": "",
  "old_price": "",
  "currency": "",
  "image": "",
  "description": "Alias ea vel libero necessitatibus perspiciatis delectus rerum soluta. Sit nulla corporis illo eligendi perspiciatis nam sequi. Officia eos recusandae ad soluta ratione quo.\nDolore occaecati aliquid similique cumque. Animi beatae consequuntur amet ab sit. Alias blanditiis quos quisquam nostrum reprehenderit quas rerum.",
  "course_level": "intermediate",
  "no_of_students": "",
  "no_of_lectures": "",
  "course_duration": "",
  "rate": "3.7",
  "in_wishlist ": 1
}*/