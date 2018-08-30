package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.InstructorReviewHolder;
import com.dp.uheadmaster.holders.QuestionAnswerInstructorHolder;
import com.dp.uheadmaster.models.Content;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.ReviewModel;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.ArrayList;

/**
 * Created by DELL on 18/10/2017.
 */

public class InstructorReviewAdapter extends RecyclerView.Adapter<InstructorReviewHolder> {

    private Context context;
    private FontChangeCrawler fontChanger;
    ArrayList<ReviewModel>reviewModels;
    public InstructorReviewAdapter(Context context, ArrayList<ReviewModel>reviewModels) {
        this.context=context;
        this.reviewModels=reviewModels;
    }

    @Override
    public InstructorReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_review_item_layout,parent,false);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)v);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)v);
        }
        return new InstructorReviewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(InstructorReviewHolder holder, int position) {
        holder.onBind(reviewModels.get(position));

    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }
}
