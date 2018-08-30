package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 05/09/2017.
 */

public class CoursesSearchResultAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CourseModel> courseModel;
    private FontChangeCrawler fontChanger;
    public CoursesSearchResultAdapter(Context context, ArrayList<CourseModel> courseModel){
       this.context=context;
        this.courseModel=courseModel;
   }
    @Override
    public int getCount() {
        return courseModel.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v=new View(context);
        v = inflater.inflate(R.layout.item_course, null);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        LinearLayout item = (LinearLayout) v.findViewById(R.id.item);
        final ImageView imgCourseImage = (ImageView) v.findViewById(R.id.img_course_logo);
        RatingBar rbRate = (RatingBar) v.findViewById(R.id.rating_bar);
        TextView tvCourseTitle= (TextView) v.findViewById(R.id.tv_course_title);
        TextView tvInstructorName= (TextView) v.findViewById(R.id.tv_instructor_name);
        TextView tvViewsCount= (TextView) v.findViewById(R.id.tv_course_views);
        TextView tvPrice= (TextView) v.findViewById(R.id.tv_course_price);
        TextView tvOldPrice= (TextView) v.findViewById(R.id.tv_course_old_price);

        try {


            tvCourseTitle.setText(courseModel.get(position).getTitle());
            tvInstructorName.setText(courseModel.get(position).getInstructorName());
            tvOldPrice.setText(courseModel.get(position).getOldProice() + courseModel.get(position).getCurrency());
            tvOldPrice.setText(courseModel.get(position).getPrice() + courseModel.get(position).getCurrency());
            tvViewsCount.setText("(" + courseModel.get(position).getViewsCount() + ")");
            float rate = Float.parseFloat(courseModel.get(position).getRate());
            rbRate.setRating(rate);

           /* item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , CourseDetailAct.class);
                    intent.putExtra("course_id" , courseModel.get(position).getId());

                    context.startActivity(intent);
                }
            });*/
            if (courseModel.get(position).getImagePath() != null && courseModel.get(position).getImagePath().isEmpty()) {
                imgCourseImage.setImageResource(R.drawable.ic_logo);
            } else {
                Picasso.with(context)
                        .load(courseModel.get(position).getImagePath())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(imgCourseImage);
            }


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Build.VERSION.SDK_INT>=21) {
                        imgCourseImage.setTransitionName("selectedImage");
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, imgCourseImage, imgCourseImage.getTransitionName());
                        Intent intent = new Intent(context, CourseDetailAct.class);
                        intent.putExtra("course_id", courseModel.get(position).getId());
                        intent.putExtra("Image_Path", courseModel.get(position).getImagePath());
                        context.startActivity(intent, activityOptionsCompat.toBundle());
                    }else {
                        Intent intent = new Intent(context, CourseDetailAct.class);
                        intent.putExtra("course_id", courseModel.get(position).getId());
                        intent.putExtra("Image_Path", courseModel.get(position).getImagePath());
                        context.startActivity(intent);
                    }
                }
            });


        } catch (Exception e) {
            System.out.println("Course / error :" + e.getMessage());
        }

        return v;
    }
}
