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

import java.util.List;

/**
 * Created by DELL on 22/08/2017.
 */

public class WishListAdapter extends BaseAdapter {

    private Context context;
    private List<CourseModel> wishCoursesList;
    private FontChangeCrawler fontChanger;
    public WishListAdapter(Context context, List<CourseModel> wishCoursesList) {
        this.context = context;
        this.wishCoursesList = wishCoursesList;
    }

    @Override
    public int getCount() {
        return wishCoursesList.size();
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
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        v = new View(context);
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
        TextView tvCourseTitle = (TextView) v.findViewById(R.id.tv_course_title);
        TextView tvInstructorName = (TextView) v.findViewById(R.id.tv_instructor_name);
        TextView tvViewsCount = (TextView) v.findViewById(R.id.tv_course_views);
        TextView tvPrice = (TextView) v.findViewById(R.id.tv_course_price);
        TextView tvOldPrice = (TextView) v.findViewById(R.id.tv_course_old_price);

        final CourseModel courseModel = wishCoursesList.get(position);
        try {

            System.out.println("Course image :" + courseModel.getImagePath() + " / rate " + courseModel.getRate());

            tvCourseTitle.setText(courseModel.getTitle());
            tvInstructorName.setText(courseModel.getInstructorName());
            tvOldPrice.setText(courseModel.getOldProice() + courseModel.getCurrency());
            tvOldPrice.setText(courseModel.getPrice() + courseModel.getCurrency());
            tvViewsCount.setText("(" + courseModel.getViewsCount() + ")");
            float rate = Float.parseFloat(courseModel.getRate());
            rbRate.setRating(rate);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Build.VERSION.SDK_INT>=21) {
                        imgCourseImage.setTransitionName("selectedImage");
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, imgCourseImage, imgCourseImage.getTransitionName());
                        Intent intent = new Intent(context, CourseDetailAct.class);
                        intent.putExtra("course_id", courseModel.getId());
                        intent.putExtra("Image_Path", courseModel.getImagePath());
                        context.startActivity(intent, activityOptionsCompat.toBundle());

                    }else {
                        Intent intent = new Intent(context, CourseDetailAct.class);
                        intent.putExtra("course_id", courseModel.getId());
                        intent.putExtra("Image_Path", courseModel.getImagePath());
                        context.startActivity(intent);

                    }
                }
            });
            if (courseModel.getImagePath() == null && courseModel.getImagePath().isEmpty()) {
                imgCourseImage.setImageResource(R.drawable.ic_course_default);
            } else {
                Picasso.with(context)


                        .load(courseModel.getImagePath())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(imgCourseImage);
            }


        } catch (Exception e) {
            System.out.println("Course / error :" + e.getMessage());
        }

        return v;

    }
}
