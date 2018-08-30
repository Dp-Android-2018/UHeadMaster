package com.dp.uheadmaster.adapters;

import android.app.ProgressDialog;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.dp.uheadmaster.models.RoadMapChild;
import com.dp.uheadmaster.models.TitleChild;

import java.util.List;

/**
 * Created by DELL on 30/12/2017.
 */

public class RoadMapParent implements Parent<RoadMapChild> {

    private String parentTitle;
    private List<RoadMapChild>roadMapChildren;

    public RoadMapParent(String parentTitle, List<RoadMapChild> roadMapChildren) {
        this.parentTitle = parentTitle;
        this.roadMapChildren = roadMapChildren;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    @Override
    public List<RoadMapChild> getChildList() {
        return roadMapChildren;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
