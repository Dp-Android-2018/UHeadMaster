package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.CourseHolder;
import com.dp.uheadmaster.holders.ReviewHolder;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 27/08/2017.
 */

public class ViewReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {
    private Context context;
    private ArrayList<ReviewModel>reviews;
    public ViewReviewAdapter(Context context, ArrayList<ReviewModel>reviews) {
        this.context=context;
        this.reviews=reviews;
    }


    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout,parent,false);
        return new ReviewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        final ReviewModel reviewModel = reviews.get(position);
        holder.tvReviewername.setText(reviewModel.getUser());
        holder.rbarValue.setRating(reviewModel.getRate());
        holder.etComment.setText(reviewModel.getComment());

    }



    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
