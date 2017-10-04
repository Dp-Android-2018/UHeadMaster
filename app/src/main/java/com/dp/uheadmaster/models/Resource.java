package com.dp.uheadmaster.models;

import android.app.ProgressDialog;

/**
 * Created by DELL on 04/10/2017.
 */

public class Resource {

    private int lectureId;

    private String lectureTitle;

    private int resourceId;

    private String resourceTitle;

    private String resourcePath;

    public Resource(int lectureId, String lectureTitle, int resourceId, String resourceTitle, String resourcePath) {
        this.lectureId = lectureId;
        this.lectureTitle = lectureTitle;
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.resourcePath = resourcePath;
    }

    public int getLectureId() {
        return lectureId;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
