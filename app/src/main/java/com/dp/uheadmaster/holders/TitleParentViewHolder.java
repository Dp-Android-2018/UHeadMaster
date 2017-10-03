package com.dp.uheadmaster.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.fragments.LecturesFrag;
import com.dp.uheadmaster.interfaces.ParentPositionChecker;
import com.dp.uheadmaster.models.TitleParent;

/**
 * Created by DELL on 16/09/2017.
 */

public class TitleParentViewHolder extends ParentViewHolder {

    public TextView tvTitle;
    public ImageView ivImage;
    public Context context;
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    public TitleParentViewHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        tvTitle=(TextView) itemView.findViewById(R.id.tv_parent_title);
        ivImage=(ImageView)itemView.findViewById(R.id.iv_parent_image);


    }


    public void bind(TitleParent titleParent){
        tvTitle.setText(titleParent.getTitle());

    }



    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                ivImage.setImageResource(R.drawable.remove);
                ParentPositionChecker parentPositionChecker=new LecturesFrag();
                parentPositionChecker.getParentPosition(context,getParentAdapterPosition());
            } else {
                ivImage.setImageResource(R.drawable.add);
            }
        }


    }




    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }
}
