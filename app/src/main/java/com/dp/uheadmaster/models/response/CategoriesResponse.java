package com.dp.uheadmaster.models.response;

import com.dp.uheadmaster.models.CategoryModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class CategoriesResponse {
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
    @SerializedName("status")
    private int status;
    @SerializedName("categories")
    private List<CategoryModel> categoriesList;

    @SerializedName("message")
    private String message;

    @SerializedName("next_page_url")
    private String nextPage;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public List<CategoryModel> getCategoriesList() {
        return categoriesList;
    }

    public String getNextPage() {
        return nextPage;
    }
}
