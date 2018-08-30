package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class CourseModel {
    /*"status": 200,
  "courses": [
    {
      "id": 1,
      "title": "something",
      "instructor": "Osama Aldemeery",
      "rate": "4.0",
      "price": "1020",
      "currency": "usd",
      "image": "http://192.168.1.100/master/uheadmaster/public/media/thumbs/thumb_logolounge-10-logo-designs-by-alex-tass_15033291682849_15034077715054.png"
    }
  ]
*/

    /*@SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("instructor")
    private String instructorName;
    @SerializedName("rate")
    private String rate;
    @SerializedName("price")
    private String price;

    @SerializedName("old_price")
    private String oldPrice;
    @SerializedName("number_of_rates")
    private String viewsCount;

    @SerializedName("currency")
    private String currency;
    @SerializedName("image")
    private String image;

    public String getOldPrice() {
        return oldPrice;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }*/


    @SerializedName("instructor")
    private String instructorName;

    public String getInstructorName() {
        return instructorName;
    }

    @SerializedName("number_of_rates")
    private String viewsCount;

    public String getViewsCount() {
        return viewsCount;
    }

    @SerializedName("id")

    private int id;
    @SerializedName("title")
    private String title;
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public int getIsInCart() {
        return isInCart;
    }

    public String getLangauge() {
        return langauge;
    }


}
