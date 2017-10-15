package com.dp.uheadmaster.models;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleChild {

    private String lectureTitle;
    private String videoPath;
    private int lectureId;
    private int contentId;
    private String contentType;
    private String contentPath;
    private String contentName;
    private String videoDuration;
    public TitleChild(int lectureId,String lectureTitle,int contentId,String contentType,String contentPath,String contentName,String videoDuration) {
        this.lectureTitle = lectureTitle;
        this.lectureId=lectureId;
        this.contentId=contentId;
        this.contentType=contentType;
        this.contentPath=contentPath;
        this.contentName=contentName;
        this.videoDuration=videoDuration;

    }


    public String getContentName() {
        return contentName;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public int getLectureId() {
        return lectureId;
    }

    public int getContentId() {
        return contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentPath() {
        return contentPath;
    }
}
