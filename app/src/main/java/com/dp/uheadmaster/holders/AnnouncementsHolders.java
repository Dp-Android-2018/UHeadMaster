package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.uheadmaster.R;

/**
 * Created by DELL on 27/09/2017.
 */

public class AnnouncementsHolders extends RecyclerView.ViewHolder {

    private TextView tvStudentName,tvAnswerDate,tvAnswer;
    private ImageView ivStudentImage;
    public AnnouncementsHolders(View itemView, Context context) {
        super(itemView);
        tvStudentName=(TextView)itemView.findViewById(R.id.tv_student_name);
        tvAnswerDate=(TextView)itemView.findViewById(R.id.tv_answer_date);
        tvAnswer=(TextView)itemView.findViewById(R.id.tv_answer);
        ivStudentImage=(ImageView)itemView.findViewById(R.id.iv_student_image);
    }

    public TextView getTvStudentName() {
        return tvStudentName;
    }

    public TextView getTvAnswerDate() {
        return tvAnswerDate;
    }

    public TextView getTvAnswer() {
        return tvAnswer;
    }

    public ImageView getIvStudentImage() {
        return ivStudentImage;
    }
}
