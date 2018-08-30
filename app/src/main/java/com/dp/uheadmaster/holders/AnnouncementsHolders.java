package com.dp.uheadmaster.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.AnnouncementDetails;
import com.dp.uheadmaster.models.AnnouncementData;
import com.dp.uheadmaster.models.response.AnnounceMentResponse;

/**
 * Created by DELL on 27/09/2017.
 */

public class AnnouncementsHolders extends RecyclerView.ViewHolder {

    private TextView tvStudentName,tvAnswerDate,tvAnswer,tvNumOfComments;
    private ImageView ivStudentImage;
    private Context context;
    public AnnouncementsHolders(View itemView, Context context,int checker) {
        super(itemView);
        this.context=context;
        tvStudentName=(TextView)itemView.findViewById(R.id.tv_student_name);
        tvAnswerDate=(TextView)itemView.findViewById(R.id.tv_answer_date);
        tvAnswer=(TextView)itemView.findViewById(R.id.tv_answer);
        ivStudentImage=(ImageView)itemView.findViewById(R.id.iv_student_image);
        tvNumOfComments=(TextView)itemView.findViewById(R.id.tv_comments_num);
        if(checker==1)
        tvNumOfComments.setVisibility(View.VISIBLE);
        else if(checker==2)
            tvNumOfComments.setVisibility(View.GONE);
    }

    public void onBind(final AnnouncementData announcementData){
     //   Toast.makeText(context, "Num Of Comments:"+announcementData.getNumOfAnnouncementComments(), Toast.LENGTH_LONG).show();
        tvStudentName.setText(announcementData.getTitle());
        tvAnswerDate.setText(announcementData.getUpatedAt());
        tvAnswer.setText(announcementData.getContent());
        tvNumOfComments.setText(announcementData.getNumOfAnnouncementComments()+" " +context.getResources().getString(R.string.comments));
        tvNumOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, AnnouncementDetails.class);
                i.putExtra("Announcement",announcementData);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    public TextView getTvNumOfComments() {
        return tvNumOfComments;
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
