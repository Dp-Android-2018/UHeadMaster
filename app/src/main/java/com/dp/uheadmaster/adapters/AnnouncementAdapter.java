package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.AnnouncementsHolders;
import com.dp.uheadmaster.models.AnnouncementData;

import java.util.ArrayList;

/**
 * Created by DELL on 24/09/2017.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementsHolders> {

    private Context context;
    private ArrayList<AnnouncementData> announcementDatas;
    public AnnouncementAdapter(Context context,ArrayList<AnnouncementData> announcementDatas) {
        this.context=context;
        this.announcementDatas=announcementDatas;


    }

    @Override
    public AnnouncementsHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.announcment_item_layout,parent,false);
        return new AnnouncementsHolders(v,context);
    }

    @Override
    public void onBindViewHolder(AnnouncementsHolders holder, int position) {
        holder.onBind(announcementDatas.get(position));

    }

    @Override
    public int getItemCount() {
        return announcementDatas.size();
    }
}
