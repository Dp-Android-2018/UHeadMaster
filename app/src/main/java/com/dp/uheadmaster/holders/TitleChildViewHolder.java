package com.dp.uheadmaster.holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.interfaces.VideoPathChecker;
import com.dp.uheadmaster.models.TitleChild;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleChildViewHolder extends ChildViewHolder {

    public TextView tvLectureName;
    private Context context;
    //public ListView listView;

    public TitleChildViewHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        //tvBullet=(TextView)itemView.findViewById(R.id.tv_bullet);
        tvLectureName=(TextView)itemView.findViewById(R.id.tv_lecture_name);

    }



    public void bind(final TitleChild titleChild){

       // tvBullet.setTextColor(context.getResources().getColor((R.color.black)));
        tvLectureName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getChildAdapterPosition();



                tvLectureName.setTextColor(context.getResources().getColor((R.color.child_active_colr)));
                //tvBullet.setTextColor(context.getResources().getColor((R.color.child_active_colr)));
               // VideoPathChecker videoPathChecker=new CourseLearn();
                //videoPathChecker.getVideoPath(context,titleChild.getVideoPath());
            }
        });

    }

}
