package com.dp.uheadmaster.holders;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.interfaces.QuizInterface;
import com.dp.uheadmaster.models.QuizAnswer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 22/10/2017.
 */

public class QuizAnswersHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_answer)
    public TextView tvanswers;

    @BindView(R.id.iv_circle)
    public ImageView ivCircle;


    private Context context;
    public QuizAnswersHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context=context;
    }

    public void onBind(QuizAnswer answer, final int position, final QuizInterface quizInterface){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "Cuurent:"+QuizAnswersHolder.this.getAdapterPosition()+" \n Prev:"+QuizAnswersHolder.this.getOldPosition()+" \n Layout:"+QuizAnswersHolder.this.getLayoutPosition(), Toast.LENGTH_SHORT).show();


                    itemView.setBackground(context.getResources().getDrawable(R.drawable.layout_quiz_answer_selected_bg));
                    tvanswers.setTextColor(context.getResources().getColor(R.color.white));
                    ivCircle.setBackground(context.getResources().getDrawable(R.drawable.ic_circle_empty_selected));


                    quizInterface.getAnswerposition(position);



                /*if(position!=QuizAnswersHolder.this.getLayoutPosition()){
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    tvanswers.setTextColor(context.getResources().getColor(R.color.qize_answer_color));
                    ivCircle.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                }*/
            }
        });
        tvanswers.setText(answer.getAnswer());

       //

    }
}
