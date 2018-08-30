package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.ResponsesAct;
import com.dp.uheadmaster.holders.CartHolder;
import com.dp.uheadmaster.holders.QuestionAnswerHolder;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 24/09/2017.
 */

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerHolder> {
    private Context context;
    private ArrayList<Question>questions;
    private int courseId;
    private FontChangeCrawler fontChanger;
    public QuestionAnswerAdapter(Context context, ArrayList<Question>questions,int courseId) {
        this.context=context;
        this.questions=questions;
        this.courseId=courseId;
    }

    @Override
    public QuestionAnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_item_layout,parent,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new QuestionAnswerHolder(v,context,1);
    }

    @Override
    public void onBindViewHolder(QuestionAnswerHolder holder, final int position) {
        try {

            holder.getTvQuestionerName().setText(questions.get(position).getUser().getName());
            holder.getTvQuestionDate().setText(questions.get(position).getCreatedAt());
            holder.getTvQuestionContent().setText(questions.get(position).getContent());
            if(questions.get(position).getTitle().equals("")||questions.get(position).getTitle()==null)
                holder.getTvQuestionTitle().setVisibility(View.GONE);
            else
            holder.getTvQuestionTitle().setText(questions.get(position).getTitle());

            holder.getTvAnswersNum().setText(questions.get(position).getAnswersCount()+" " +context.getString(R.string.response));
            if (questions.get(position).getUser().getImageLink() != null && !questions.get(position).getUser().getImageLink().equals(""))
                Picasso.with(context).load(questions.get(position).getUser().getImageLink()).into(holder.getIvQusetionerImage());
            else
                holder.getIvQusetionerImage().setImageResource(R.drawable.ic_logo);
            holder.getTvAnswersNum().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, ResponsesAct.class);
                    i.putExtra("Question",questions.get(position));
                    i.putExtra("CourseId",courseId);
                    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
