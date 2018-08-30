package com.dp.uheadmaster.holders;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.RoadMapChild;

/**
 * Created by DELL on 30/12/2017.
 */

public class RoadMapChildViewHolder extends ChildViewHolder {


    private Context context;
    private TextView tvContent;
    public RoadMapChildViewHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        tvContent=(TextView)itemView.findViewById(R.id.tv_roadmap_content);
    }

    public void bind(RoadMapChild roadMapChild){

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N)
                tvContent.setText(Html.fromHtml(roadMapChild.getContent()));
        else
            tvContent.setText(Html.fromHtml(roadMapChild.getContent(),Html.FROM_HTML_MODE_LEGACY));
    }

}
