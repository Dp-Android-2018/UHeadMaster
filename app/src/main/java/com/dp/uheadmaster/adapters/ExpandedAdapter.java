package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.TitleChildViewHolder;
import com.dp.uheadmaster.holders.TitleParentViewHolder;
import com.dp.uheadmaster.interfaces.VideoLinksInterface;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.models.TitleParent;

import java.util.List;

/**
 * Created by DELL on 16/09/2017.
 */

public class ExpandedAdapter extends ExpandableRecyclerAdapter<TitleParent,TitleChild,TitleParentViewHolder,TitleChildViewHolder> {


    private LayoutInflater inflater;
    private VideoLinksInterface videoLinksInterface;
    private Context context;
    public ExpandedAdapter(Context context, @NonNull List<TitleParent> parentList,VideoLinksInterface videoLinksInterface) {
        super(parentList);
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.videoLinksInterface=videoLinksInterface;
    }

    @NonNull
    @Override
    public TitleParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.recycler_view_parent_item_layout,parentViewGroup,false);
        return new TitleParentViewHolder(context,v);
    }

    @NonNull
    @Override
    public TitleChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.recycler_view_child_layout,childViewGroup,false);
        return new TitleChildViewHolder(context,v,videoLinksInterface);
    }

    @Override
    public void onBindParentViewHolder(@NonNull TitleParentViewHolder parentViewHolder, int parentPosition, @NonNull TitleParent parent) {
        parentViewHolder.bind(parent);

    }

    @Override
    public void onBindChildViewHolder(@NonNull TitleChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull TitleChild child) {
        childViewHolder.bind(child);

    }





}
