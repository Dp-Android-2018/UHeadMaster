package com.dp.uheadmaster.holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.interfaces.VideoLinksInterface;
import com.dp.uheadmaster.interfaces.VideoPathChecker;
import com.dp.uheadmaster.models.TitleChild;

import es.dmoral.toasty.Toasty;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleChildViewHolder extends ChildViewHolder {

    private TextView tvLectureName;
    private TextView tvLectureType;
    private TextView tvVideoDuration;
    private Context context;
    private VideoLinksInterface videoLinksInterface;
    //public ListView listView;

    public TitleChildViewHolder(Context context, View itemView, VideoLinksInterface videoLinksInterface) {
        super(itemView);
        this.context=context;
        this.videoLinksInterface=videoLinksInterface;
        tvLectureName=(TextView)itemView.findViewById(R.id.tv_lecture_name);
        tvLectureType=(TextView)itemView.findViewById(R.id.tv_lecture_type);
        tvVideoDuration=(TextView)itemView.findViewById(R.id.tv_lecture_duration);
    }



    public void bind(final TitleChild titleChild){
       // Toasty.success(context,"Content Type >>>> "+titleChild.getContentType(), Toast.LENGTH_LONG).show();
        tvLectureName.setText(titleChild.getLectureTitle());
        tvLectureName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChildAdapterPosition();
                tvLectureName.setTextColor(context.getResources().getColor((R.color.child_active_colr)));

            }
        });


        if(titleChild.getContentType().equals("doc")) {
            tvLectureType.setText("Doc");
            tvVideoDuration.setVisibility(View.GONE);
        }else if(titleChild.getContentType().equals("video")) {
            tvLectureType.setText("Video");
            tvVideoDuration.setText(titleChild.getVideoDuration());
            tvVideoDuration.setVisibility(View.VISIBLE);
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleChild.getContentType().equals("video")){
                   videoLinksInterface.getVideoName(titleChild.getContentPath());

                }
            }
        });

    }

}
