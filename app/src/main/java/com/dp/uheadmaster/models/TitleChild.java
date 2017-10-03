package com.dp.uheadmaster.models;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleChild {

    private String lectureTitle;
    private String videoPath;

    public TitleChild(String lectureTitle,String videoPath) {
        this.lectureTitle = lectureTitle;
        this.videoPath=videoPath;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
