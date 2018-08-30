package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.QuestionAnswerInstructorHolder;
import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.ArrayList;

import retrofit.http.POST;

/**
 * Created by DELL on 18/10/2017.
 */

public class InstructorQuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerInstructorHolder> {

    private Context context;
    private ArrayList<Question>questions;
    Activity activity;
    public InstructorQuestionAnswerAdapter(Context context, ArrayList<Question>questions, Activity activity) {
        this.context=context;
        this.questions=questions;
        this.activity=activity;
    }
    private FontChangeCrawler fontChanger;

    @Override
    public QuestionAnswerInstructorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_instructor_item_layout,parent,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new QuestionAnswerInstructorHolder(v,context,activity);
    }

    @Override
    public void onBindViewHolder(QuestionAnswerInstructorHolder holder, int position) {
        holder.onBind(questions.get(position));

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
