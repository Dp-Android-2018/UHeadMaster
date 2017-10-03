package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.QuestionAnswerHolder;
import com.dp.uheadmaster.holders.ResourcesHolder;

/**
 * Created by DELL on 24/09/2017.
 */

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesHolder> {
    private Context context;
    public ResourcesAdapter(Context context) {
        this.context=context;
    }

    @Override
    public ResourcesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.resources_item_layout,parent,false);
        return new ResourcesHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ResourcesHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}