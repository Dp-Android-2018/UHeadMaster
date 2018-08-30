package com.dp.uheadmaster.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.ReviewModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 18/10/2017.
 */

public class InstructorReviewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tv_reviewer_name)
    TextView tvReviewerName;


    @BindView(R.id.tv_review)
    TextView tvReview;

    @BindView(R.id.iv_reviewer_image)
    ImageView ivReviewerImage;


    @BindView(R.id.rating_value)
    RatingBar ratingValue;



    private Context context;
    public InstructorReviewHolder(View itemView,Context context) {
        super(itemView);
        this.context=context;
        ButterKnife.bind(context,itemView);
    }

    public void onBind(final ReviewModel reviewModel){
       /* itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Toast:"+reviewModel.getId(), Toast.LENGTH_SHORT).show();
            }
        });*/
        tvReviewerName.setText(reviewModel.getUser());
        tvReview.setText(reviewModel.getComment());
        ratingValue.setRating(Float.parseFloat(reviewModel.getRate()));
        if(reviewModel.getUserImage()!=null && !reviewModel.getUserImage().equals(""))
            Picasso.with(context).load(reviewModel.getUserImage()).into(ivReviewerImage);

    }
}
