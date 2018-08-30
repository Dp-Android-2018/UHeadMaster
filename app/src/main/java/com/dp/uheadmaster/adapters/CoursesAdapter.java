package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.holders.CourseHolder;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by DELL on 22/08/2017.
 */

public class CoursesAdapter extends RecyclerView.Adapter<CourseHolder> {
    private Context context;
    private List<CourseModel> coursesList;
    private FontChangeCrawler fontChanger;
    private CourseHolder holder;
    private boolean isFirstTime=true;
    public CoursesAdapter(Context context, List<CourseModel> coursesList) {
        this.context = context;
        this.coursesList = coursesList;

    }

    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new CourseHolder(v,context);
    }

    @Override
    public void onBindViewHolder(final CourseHolder holder, int position) {
        this.holder=holder;
        if(!coursesList.isEmpty()) {
            final CourseModel courseModel = coursesList.get(position);
       /* if(position==coursesList.size()-1){
            Toast.makeText(context, "Price:"+courseModel.getPrice() +" \n Old Price:"+ courseModel.getOldProice()+"\n iD :"+courseModel.getId(), Toast.LENGTH_SHORT).show();
        }*/
            try {


                if(isFirstTime) {
                    ((LoaderTextView) holder.tvCourseTitle).resetLoader();
                    ((LoaderTextView) holder.tvInstructorName).resetLoader();
                    ((LoaderTextView) holder.tvOldPrice).resetLoader();
                    ((LoaderTextView) holder.tvPrice).resetLoader();
                    ((LoaderTextView) holder.tvViewsCount).resetLoader();
                    ((LoaderImageView) holder.imgCourseImage).resetLoader();
                    isFirstTime=false;
                }else {

                }
                System.out.println("Null Exp :"+courseModel.getViewsCount());
                holder.tvCourseTitle.setText(courseModel.getTitle());
                holder.tvInstructorName.setText(courseModel.getInstructorName());
                holder.tvOldPrice.setText(courseModel.getOldProice() + courseModel.getCurrency());
                holder.tvPrice.setText(courseModel.getPrice() + courseModel.getCurrency());
                holder.tvViewsCount.setText("(" + courseModel.getViewsCount() + ")");
                float rate = Float.parseFloat(courseModel.getRate());
                holder.rbRate.setRating(rate);

                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Build.VERSION.SDK_INT>=21) {
                            holder.imgCourseImage.setTransitionName("selectedImage");
                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.imgCourseImage, holder.imgCourseImage.getTransitionName());
                            Intent intent = new Intent(context, CourseDetailAct.class);
                            intent.putExtra("course_id", courseModel.getId());
                            intent.putExtra("Image_Path", courseModel.getImagePath());
                            //  Toast.makeText(context, "Cid :"+courseModel.getId(), Toast.LENGTH_SHORT).show();
                            context.startActivity(intent, activityOptionsCompat.toBundle());
                        }else {
                            Intent intent = new Intent(context, CourseDetailAct.class);
                            intent.putExtra("course_id", courseModel.getId());
                            intent.putExtra("Image_Path", courseModel.getImagePath());

                            context.startActivity(intent);
                        }
                    }
                });
                if (courseModel.getImagePath() != null && courseModel.getImagePath().isEmpty()) {
                    holder.imgCourseImage.setImageResource(R.drawable.ic_course_default);
                } else {
                    Picasso.with(context)
                            .load(courseModel.getImagePath())
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                            .into(holder.imgCourseImage);
                }


            } catch (Exception e) {
                System.out.println("Course / error :" + e.getMessage());
            }
        }

    }


    @Override
    public int getItemCount() {
        if(coursesList.isEmpty())
            return 5;
        else {
           /* ((LoaderTextView)holder.tvCourseTitle).resetLoader();
            ((LoaderTextView)holder.tvInstructorName).resetLoader();
            ((LoaderTextView) holder.tvOldPrice).resetLoader();
            ((LoaderTextView)holder.tvPrice).resetLoader();
            ((LoaderTextView)holder.tvViewsCount).resetLoader();
            ((LoaderImageView)holder.imgCourseImage).resetLoader();*/
            return coursesList.size();
        }
    }


}

