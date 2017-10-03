package com.dp.uheadmaster.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;

/**
 * Created by DELL on 06/09/2017.
 */

public class SearchCategoryHolder extends RecyclerView.ViewHolder{


    public TextView title;

    public SearchCategoryHolder(View v) {
        super(v);


        title = (TextView) v.findViewById(R.id.tv_title);


    }
}
