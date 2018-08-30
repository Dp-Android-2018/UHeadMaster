package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.AnnouncementsHolders;
import com.dp.uheadmaster.holders.QuestionAnswerHolder;
import com.dp.uheadmaster.models.Answer;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 26/09/2017.
 */

public class ResponsesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Answer>answers;
    private Question question;
    private FontChangeCrawler fontChanger;
    public ResponsesAdapter(Context context, Question question, ArrayList<Answer>answers) {
        this.context=context;
        this.answers=answers;
        this.question=question;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        RecyclerView.ViewHolder holder=null;
        switch (viewType)
        {
            case 1:
                    view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_item_layout,parent,false);
                    holder=new QuestionAnswerHolder(view,context,2);
                break;
            case 2:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.announcment_item_layout,parent,false);
                holder=new AnnouncementsHolders(view,context,2);
                break;
            default:
        }


        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)view);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 1:
                try {

                    QuestionAnswerHolder questionAnswerHolder=(QuestionAnswerHolder)holder;
                    if(question.getTitle().equals("")||question.getTitle()==null)
                        questionAnswerHolder.getTvQuestionTitle().setVisibility(View.GONE);
                    else
                        questionAnswerHolder.getTvQuestionTitle().setText(question.getTitle());

                    questionAnswerHolder.getTvAnswersNum().setText(question.getAnswersCount()+" " +context.getString(R.string.response));
                    questionAnswerHolder.getTvQuestionerName().setText(question.getUser().getName());
                    questionAnswerHolder.getTvQuestionDate().setText(question.getCreatedAt());
                    questionAnswerHolder.getTvQuestionContent().setText(question.getContent());
                    if (question.getUser().getImageLink() != null && !question.getUser().getImageLink().equals(""))
                        Picasso.with(context).load(question.getUser().getImageLink()).into(questionAnswerHolder.getIvQusetionerImage());

                }  catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                break;
            case 2:
                try {

                    AnnouncementsHolders announcementsHolders=(AnnouncementsHolders)holder;
                    announcementsHolders.getTvNumOfComments().setVisibility(View.GONE);
                    announcementsHolders.getTvStudentName().setText(answers.get(position-1).getUser().getName());
                    announcementsHolders.getTvAnswerDate().setText(answers.get(position-1).getCreatedAt());
                    announcementsHolders.getTvAnswer().setText(answers.get(position-1).getContent());
                    if (question.getUser().getImageLink() != null && !question.getUser().getImageLink().equals("")) {
                        Picasso.with(context).load(answers.get(position - 1).getUser().getImageLink()).into(announcementsHolders.getIvStudentImage());
                    }else{
                        announcementsHolders.getIvStudentImage().setImageResource(R.drawable.ic_logo);
                    }

                }  catch (NullPointerException ex){
                    System.out.println("Catch Message :"+ex.getMessage());
                    ex.printStackTrace();
                }catch (Exception ex){
                    System.out.println("Catch2 Message :"+ex.getMessage());
                    ex.printStackTrace();
                }
                break;
        }

    }






    @Override
    public int getItemCount() {
        return answers.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return 1;
        else
            return 2;

    }
}
