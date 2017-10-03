package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.Locale;

/**
 * Created by Ahmed on 8/23/2017.
 */

public class CourseHolder extends RecyclerView.ViewHolder{


    public LinearLayout item;
    public ImageView imgCourseImage;
    public TextView tvCourseTitle , tvInstructorName , tvViewsCount , tvPrice , tvOldPrice;
    public RatingBar rbRate;
    public CourseHolder(View v, Context context) {
        super(v);
        this.item = (LinearLayout) v.findViewById(R.id.item);
        this.imgCourseImage = (ImageView) v.findViewById(R.id.img_course_logo);
        this.rbRate = (RatingBar) v.findViewById(R.id.rating_bar);
        this.tvCourseTitle= (TextView) v.findViewById(R.id.tv_course_title);
        this.tvInstructorName= (TextView) v.findViewById(R.id.tv_instructor_name);
        this.tvViewsCount= (TextView) v.findViewById(R.id.tv_course_views);
        this.tvPrice= (TextView) v.findViewById(R.id.tv_course_price);
        this.tvOldPrice= (TextView) v.findViewById(R.id.tv_course_old_price);

            if ( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
                this.tvCourseTitle.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font1"));
                this.tvInstructorName.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("en_font2"));
            } else if ( ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
                this.tvCourseTitle.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
                this.tvInstructorName.setTypeface(ApplyCustomFont.getInstance(context).chooseFont("ar_font"));
            }




    }


}