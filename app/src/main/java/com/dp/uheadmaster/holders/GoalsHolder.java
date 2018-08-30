package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.utilities.ConfigurationFile;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class GoalsHolder extends RecyclerView.ViewHolder {

     public TextView tvanswer;

    public GoalsHolder(View v, Context context) {
        super(v);

        tvanswer = (TextView) v.findViewById(R.id.tv_answer);
         if( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
             tvanswer.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font1"));
                 }else if( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
             tvanswer.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
              }

    }


}