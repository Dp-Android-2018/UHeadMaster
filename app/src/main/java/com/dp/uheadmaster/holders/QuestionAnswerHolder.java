package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;

/**
 * Created by DELL on 24/09/2017.
 */

public class QuestionAnswerHolder extends RecyclerView.ViewHolder {
    private TextView tvQuestionerName,tvQuestionDate,tvQuestionTitle,tvQuestionContent,tvAnswersNum,tvCommentsNum;

    private ImageView ivQusetionerImage;

    public TextView getTvCommentsNum() {
        return tvCommentsNum;
    }

    public QuestionAnswerHolder(View itemView, Context context,int checker) {
        super(itemView);
        tvQuestionerName=(TextView)itemView.findViewById(R.id.tv_student_name);
        tvQuestionDate=(TextView)itemView.findViewById(R.id.tv_question_date);
        tvQuestionTitle=(TextView)itemView.findViewById(R.id.tv_question_title);
        tvQuestionContent=(TextView)itemView.findViewById(R.id.tv_question);
        tvAnswersNum=(TextView)itemView.findViewById(R.id.tv_responses_num);
        ivQusetionerImage=(ImageView)itemView.findViewById(R.id.iv_student_image);
        tvCommentsNum=(TextView)itemView.findViewById(R.id.tv_comments_num);
        if(checker==1)
            tvAnswersNum.setVisibility(View.VISIBLE);
        else if(checker==2)
            tvAnswersNum.setVisibility(View.GONE);

    }

    public TextView getTvQuestionerName() {
        return tvQuestionerName;
    }

    public TextView getTvQuestionDate() {
        return tvQuestionDate;
    }

    public TextView getTvQuestionTitle() {
        return tvQuestionTitle;
    }

    public TextView getTvQuestionContent() {
        return tvQuestionContent;
    }

    public TextView getTvAnswersNum() {
        return tvAnswersNum;
    }

    public ImageView getIvQusetionerImage() {
        return ivQusetionerImage;
    }
}
