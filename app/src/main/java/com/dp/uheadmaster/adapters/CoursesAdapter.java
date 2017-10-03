package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.holders.CourseHolder;
import com.dp.uheadmaster.models.CourseModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by DELL on 22/08/2017.
 */

public class CoursesAdapter extends RecyclerView.Adapter<CourseHolder> {
    private Context context;
    private List<CourseModel> coursesList;

    public CoursesAdapter(Context context, List<CourseModel> coursesList) {
        this.context = context;
        this.coursesList = coursesList;
    }

    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseHolder(v,context);
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position) {
        final CourseModel courseModel = coursesList.get(position);
        try {

            System.out.println("Course image :"+courseModel.getImagePath()+" / rate "+courseModel.getRate());

            holder.tvCourseTitle.setText(courseModel.getTitle());
            holder.tvInstructorName.setText(courseModel.getInstructorName());
            holder.tvOldPrice.setText(courseModel.getOldProice() + courseModel.getCurrency());
            holder.tvOldPrice.setText(courseModel.getPrice() + courseModel.getCurrency());
            holder.tvViewsCount.setText("(" + courseModel.getViewsCount() + ")");
            float rate = Float.parseFloat(courseModel.getRate());
            holder.rbRate.setRating(rate);

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Intent intent = new Intent(context , CourseDetailAct.class);
                    intent.putExtra("course_id" , courseModel.getId());

                    context.startActivity(intent);
                }
            });
            if (courseModel.getImagePath() != null && courseModel.getImagePath().isEmpty()) {
                holder.imgCourseImage.setImageResource(R.drawable.ic_logo);
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


    @Override
    public int getItemCount() {
        return coursesList.size();
    }


}

