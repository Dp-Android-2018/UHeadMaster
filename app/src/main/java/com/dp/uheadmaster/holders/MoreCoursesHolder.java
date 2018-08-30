package com.dp.uheadmaster.holders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.InstructorCourseContent;
import com.dp.uheadmaster.models.InstructorCourse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 17/10/2017.
 */

public class MoreCoursesHolder extends RecyclerView.ViewHolder {
    private Context context;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;

    @BindView(R.id.tv_course_describtion)
    TextView tvCourseDescription;


    @BindView(R.id.tv_lecture_num)
    TextView tvLectureNum;


    @BindView(R.id.tv_videos_num)
    TextView tvVideosNum;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_price)
    TextView  tvPrice;

    @BindView(R.id.imageView2)
    ImageView ivImageView;

    public MoreCoursesHolder(Context context,View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context=context;
    }

    public void onbind(final ArrayList<InstructorCourse>courses, final int position){
        String status="";
        String price="";

        tvCourseName.setText(courses.get(position).getTitle());
        tvCourseDescription.setText(courses.get(position).getDescription());
        tvLectureNum.setText(courses.get(position).getNumOfLectures()+" "+context.getString(R.string.ilec));
       tvVideosNum.setText( courses.get(position).getNumOfvideos() +" "+context.getString(R.string.videos));
        if(courses.get(position).getStatus().equals("published")){
            status=context.getResources().getString(R.string.published);
        }else if(courses.get(position).getStatus().equals("draft")){
            status=context.getResources().getString(R.string.draft);
        }else if(courses.get(position).getStatus().equals("pending")){
            status=context.getResources().getString(R.string.pending);

        }else if(courses.get(position).getStatus().equals("rejected")){
            status=context.getResources().getString(R.string.rejected);

        }

        if(courses.get(position).getPrice()!=null){
            price=courses.get(position).getPrice();

        }else {
            price=context.getResources().getString(R.string.free);
        }
        tvStatus.setText(status);
        tvPrice.setText(price);
        if(courses.get(position).getImage()!=null && !courses.get(position).getImage().equals(""))
            Picasso.with(context).load(courses.get(position).getImage()).into(ivImageView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(context, InstructorCourseContent.class);
                     i.putExtra("course_obj" ,  courses.get(position));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }catch (ArrayIndexOutOfBoundsException ex){
                    ex.printStackTrace();
                }
            }
        });
    }
}
