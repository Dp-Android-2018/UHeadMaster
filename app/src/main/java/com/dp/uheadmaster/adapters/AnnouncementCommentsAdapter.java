package com.dp.uheadmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.holders.AnnouncementsHolders;
import com.dp.uheadmaster.holders.QuestionAnswerHolder;
import com.dp.uheadmaster.models.AnnouncementData;
import com.dp.uheadmaster.models.Answer;
import com.dp.uheadmaster.models.Comment;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Question;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DELL on 29/10/2017.
 */

public class AnnouncementCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;
    private AnnouncementData announcementData;
    private FontChangeCrawler fontChanger;
    public AnnouncementCommentsAdapter(Context context, AnnouncementData announcementData, ArrayList<Comment> comments) {
        this.context=context;
        this.comments=comments;
        this.announcementData=announcementData;
        //Toast.makeText(context, "Id:"+announcementData.getId()+"\n"+announcementData.getContent(), Toast.LENGTH_LONG).show();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        RecyclerView.ViewHolder holder=null;
        switch (viewType)
        {
            case 1:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.announcment_item_layout,parent,false);
                holder=new AnnouncementsHolders(view,context,2);
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

                    AnnouncementsHolders announcementsHolders=(AnnouncementsHolders) holder;
                    announcementsHolders.getTvStudentName().setText(announcementData.getTitle());
                    announcementsHolders.getTvAnswerDate().setText(announcementData.getUpatedAt());
                    announcementsHolders.getTvAnswer().setText(announcementData.getContent());
                    announcementsHolders.getTvNumOfComments().setText(announcementData.getNumOfAnnouncementComments()+" " +context.getResources().getString(R.string.comments));
                }  catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                break;
            case 2:
                try {

                    AnnouncementsHolders announcementsHolders=(AnnouncementsHolders) holder;
                    announcementsHolders.getTvStudentName().setText(comments.get(position-1).getUser().getName());
                    announcementsHolders.getTvAnswerDate().setText(comments.get(position-1).getCreatedAt());
                    announcementsHolders.getTvAnswer().setText(comments.get(position-1).getComment());
                    if(comments.get(position - 1).getUser().getImageLink()!=null&& !comments.get(position - 1).getUser().getImageLink().equals("")) {
                        Picasso.with(context).load(comments.get(position - 1).getUser().getImageLink()).into(announcementsHolders.getIvStudentImage());
                    }else {

                        announcementsHolders.getIvStudentImage().setImageResource(R.drawable.ic_logo);

                    }
                    announcementsHolders.getTvNumOfComments().setVisibility(View.GONE);
                }  catch (NullPointerException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
        }

    }






    @Override
    public int getItemCount() {
        return comments.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return 1;
        else
            return 2;

    }
}

