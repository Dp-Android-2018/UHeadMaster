package com.dp.uheadmaster.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class
CategoryModel {
    /*"status": 200,
"categories": [
  {
"id": 1,
"title": "Development",
"image": "http://localhost/uhead-master/public/media/images/analytics-automation_15033095789806.png"
},
  {
"id": 2,
"title": "Business",
"image": "http://localhost/uhead-master/public/media/images/academics_15033096194661.png"
}
],
*/
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
