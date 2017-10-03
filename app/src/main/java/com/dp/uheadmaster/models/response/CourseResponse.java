package com.dp.uheadmaster.models.response;

 import com.dp.uheadmaster.models.CourseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class CourseResponse {

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

    @SerializedName("status")
    private int status;
    @SerializedName("courses")
    private List<CourseModel> coursesList;

    @SerializedName("message")
    private String message;

    @SerializedName("next_page_url")
    private String nextPage;

    public String getNextPage() {
        return nextPage;
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
