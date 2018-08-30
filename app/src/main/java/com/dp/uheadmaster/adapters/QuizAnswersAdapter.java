package com.dp.uheadmaster.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.QuizAnswersHolder;
import com.dp.uheadmaster.interfaces.QuizInterface;
import com.dp.uheadmaster.models.QuizAnswer;

import java.util.ArrayList;

/**
 * Created by DELL on 22/10/2017.
 */

public class QuizAnswersAdapter extends RecyclerView.Adapter<QuizAnswersHolder> {

    private Context context;
    private ArrayList<QuizAnswer>answers;
    private QuizInterface quizInterface;
    public QuizAnswersAdapter(Context context, ArrayList<QuizAnswer>answers, QuizInterface quizInterface) {
        this.context=context;
        this.answers=answers;
        this.quizInterface=quizInterface;
    }

    @Override
    public QuizAnswersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_answer,parent,false);
        return new QuizAnswersHolder(v,context);
    }

    @Override
    public void onBindViewHolder(QuizAnswersHolder holder, int position) {

        holder.onBind(answers.get(position),position,quizInterface);



    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
}
