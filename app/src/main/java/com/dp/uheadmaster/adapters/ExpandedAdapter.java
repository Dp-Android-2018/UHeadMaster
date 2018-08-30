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
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.models.TitleParent;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.List;

/**
 * Created by DELL on 16/09/2017.
 */

public class ExpandedAdapter extends ExpandableRecyclerAdapter<TitleParent,TitleChild,TitleParentViewHolder,TitleChildViewHolder> {


    private LayoutInflater inflater;
    private VideoLinksInterface videoLinksInterface;
    private Context context;
    private FontChangeCrawler fontChanger;
    private String lastWatched;
    public ExpandedAdapter(Context context, @NonNull List<TitleParent> parentList,VideoLinksInterface videoLinksInterface,String lastWatched) {
        super(parentList);
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.videoLinksInterface=videoLinksInterface;
        this.lastWatched=lastWatched;
    }

    @NonNull
    @Override
    public TitleParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.recycler_view_parent_item_layout,parentViewGroup,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new TitleParentViewHolder(context,v);
    }

    @NonNull
    @Override
    public TitleChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View v=inflater.inflate(R.layout.recycler_view_child_layout,childViewGroup,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new TitleChildViewHolder(context,v,videoLinksInterface,lastWatched);
    }

    @Override
    public void onBindParentViewHolder(@NonNull TitleParentViewHolder parentViewHolder, int parentPosition, @NonNull TitleParent parent) {
        parentViewHolder.bind(parent);

    }

    @Override
    public void onBindChildViewHolder(@NonNull TitleChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull TitleChild child) {
        childViewHolder.bind(child,childPosition);

    }





}
