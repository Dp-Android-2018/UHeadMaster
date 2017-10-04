package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.Resource;

/**
 * Created by DELL on 27/09/2017.
 */

public class ResourcesHolder extends RecyclerView.ViewHolder {

    private TextView tvResourceName;
    public ResourcesHolder(View itemView, Context context) {
        super(itemView);
        tvResourceName=(TextView)itemView.findViewById(R.id.tv_resource_name);
    }

    public void onBind(Resource resource){
        tvResourceName.setText(resource.getLectureTitle()+" - "+resource.getResourceTitle());
    }
}
