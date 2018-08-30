package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.MoreCoursesHolder;
import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.InstructorCourse;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by DELL on 17/10/2017.
 */

public class MoreCoursesAdapter extends RecyclerView.Adapter<MoreCoursesHolder> {

    private Context context;
    private ArrayList<InstructorCourse> courses;
    public MoreCoursesAdapter(Context context,ArrayList<InstructorCourse> courses) {
        this.context=context;
        this.courses=courses;
    }
    private FontChangeCrawler fontChanger;
    @Override
    public MoreCoursesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_course_item_layout,parent,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new MoreCoursesHolder(context,v);
    }

    @Override
    public void onBindViewHolder(MoreCoursesHolder holder, int position) {

        holder.onbind(courses,position);

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
