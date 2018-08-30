package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.QuestionAnswerHolder;
import com.dp.uheadmaster.holders.ResourcesHolder;
import com.dp.uheadmaster.interfaces.OnItemClickInterface;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Resource;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.ArrayList;

/**
 * Created by DELL on 24/09/2017.
 */

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesHolder> {
    private Context context;
    private ArrayList<Resource>resources;
    private OnItemClickInterface listener;
    private FontChangeCrawler fontChanger;
    private Activity activity;
    public ResourcesAdapter(Context context, ArrayList<Resource>resources, OnItemClickInterface listener, Activity activity) {
        this.context=context;
        this.resources=resources;
        this.listener=listener;
        this.activity=activity;
    }

    @Override
    public ResourcesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.resources_item_layout,parent,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new ResourcesHolder(v,context,activity);
    }

    @Override
    public void onBindViewHolder(ResourcesHolder holder, int position) {
        holder.onBind(resources.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return resources.size();
    }
}
