package com.dp.uheadmaster.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 16/08/2017.
 */

public class SubCategoryRequest {

    @SerializedName("category_id")
    private int categoryID;


    public SubCategoryRequest(int categoryID) {
        this.categoryID = categoryID;

    }
}
