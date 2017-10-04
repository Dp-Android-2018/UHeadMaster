package com.dp.uheadmaster.models;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleParent implements Parent<TitleChild>{
    List<Object>mChildrenlist;
    private String title;
    private int id;
    private List<TitleChild>childs;

    public TitleParent(int id,String title,List<TitleChild>childs) {
        this.title=title;
        this.childs=childs;
        this.id=id;


    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public List<TitleChild> getChildList() {
        return childs;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

}
