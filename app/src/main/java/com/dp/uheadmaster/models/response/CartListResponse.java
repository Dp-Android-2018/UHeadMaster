package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.CourseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 8/27/2017.
 */

public class CartListResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("courses")
    private List<CourseModel> coursesList;

    @SerializedName("message")
    private String message;

    @SerializedName("next_page_url")
    private String nextPagePath;


    public String getNextPagePath() {
        return nextPagePath;
    }

    public int getStatus() {
        return status;
    }

    public List<CourseModel> getCoursesList() {
        return coursesList;
    }

    public String getMessage() {
        return message;
    }
}
/*{
  "status": 200,
  "courses": [
    {
      "id": 1,
      "title": "Quae hic repellat id ullam.",
      "instructor": "mahmoud",
      "rate": "3.7",
      "number_of_rates": 521,
      "price": "",
      "old_price": "",
      "currency": "",
      "image": ""
    }
  ],
  "next_page_url": "http://192.168.1.100/master/uheadmaster/public/api/wishlist?page=2"
}*/