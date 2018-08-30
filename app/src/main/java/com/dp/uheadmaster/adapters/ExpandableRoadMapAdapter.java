package com.dp.uheadmaster.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.RoadMapChildViewHolder;
import com.dp.uheadmaster.holders.RoadMapParentViewHolder;
import com.dp.uheadmaster.models.RoadMapChild;

import java.util.List;

/**
 * Created by DELL on 30/12/2017.
 */

public class ExpandableRoadMapAdapter extends ExpandableRecyclerAdapter<RoadMapParent,RoadMapChild,RoadMapParentViewHolder,RoadMapChildViewHolder> {

    private Context context;
    private List<RoadMapParent>parentList;
    private LayoutInflater inflater;

    public ExpandableRoadMapAdapter(Context context, List<RoadMapParent> parentList) {
        super(parentList);
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.parentList=parentList;
    }

    @NonNull
    @Override
    public RoadMapParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.item_road_map_parent_layout,parentViewGroup,false);

        return new RoadMapParentViewHolder(context,v);
    }

    @NonNull
    @Override
    public RoadMapChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.item_roadmap,childViewGroup,false);
        return new RoadMapChildViewHolder(context,v);
    }

    @Override
    public void onBindParentViewHolder(@NonNull RoadMapParentViewHolder parentViewHolder, int parentPosition, @NonNull RoadMapParent parent) {
        parentViewHolder.bind(parent);

    }

    @Override
    public void onBindChildViewHolder(@NonNull RoadMapChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull RoadMapChild child) {
        childViewHolder.bind(child);

    }
}
