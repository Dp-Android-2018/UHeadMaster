package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.Locale;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class CartHolder extends RecyclerView.ViewHolder {

    public ImageView imgCourseLogo, imgRemove;
    public TextView tvTitle, tvPrice, tvOldPrice;

    public CartHolder(View v, Context context) {
        super(v);

        imgCourseLogo = (ImageView) v.findViewById(R.id.img_cart_course);
        imgRemove = (ImageView) v.findViewById(R.id.img_cart_remove);
        tvTitle = (TextView) v.findViewById(R.id.tv_cart_course_title);
        tvPrice = (TextView) v.findViewById(R.id.tv_cart_price);
        tvOldPrice = (TextView) v.findViewById(R.id.tv_cart_old_price);
        if( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
            tvTitle.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font1"));
            tvPrice.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font1"));
            tvOldPrice.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font1"));
        }else if( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tvTitle.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
            tvPrice.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
            tvOldPrice.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
        }

    }


}