package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.GoalsHolder;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.GoalAnswerModel;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.List;

/**
 * Created by DELL on 21/08/2017.
 */

public class CourseGoalsAdapter extends RecyclerView.Adapter<GoalsHolder> {
    private Context context;
    private List<GoalAnswerModel> answerModels;

     private FontChangeCrawler fontChanger;
    public CourseGoalsAdapter(Context context, List<GoalAnswerModel> answerModels) {
        this.context = context;
         this.answerModels = answerModels;
     }

    @Override
    public GoalsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_goals, parent, false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new GoalsHolder(v,context);
    }

    @Override
    public void onBindViewHolder(GoalsHolder holder, int position) {

        final GoalAnswerModel courseModel = answerModels.get(position);
        holder.tvanswer.setText(courseModel.getAnswer());


    }


    @Override
    public int getItemCount() {
        return answerModels.size();
    }




}
