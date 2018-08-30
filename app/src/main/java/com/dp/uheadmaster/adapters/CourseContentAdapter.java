package com.dp.uheadmaster.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.AnnounceMentAct;
import com.dp.uheadmaster.activites.AutomaticMessageAct;
import com.dp.uheadmaster.activites.ConversionAnalysisAct;
import com.dp.uheadmaster.activites.InstructorReviewAct;
import com.dp.uheadmaster.activites.QuestionAnswerInstructor;
import com.dp.uheadmaster.activites.StudentEngagment;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 17/10/2017.
 */

public class CourseContentAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String>titles;
    @BindView(R.id.tv_category_title)
    TextView tvCategoryTitle;

    private int courseId;
    private FontChangeCrawler fontChanger;
    public CourseContentAdapter(Context context, ArrayList<String>titles,int courseId) {
        this.context=context;
        this.titles=titles;
        this.courseId=courseId;

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.instructor_course_grid_item_layout,parent,false);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(Build.VERSION.SDK_INT>=21) {
            float maxWidthOffset = 2f * context.getResources().getDisplayMetrics().widthPixels;
            float maxHeightOffset = 2f * context.getResources().getDisplayMetrics().heightPixels;
            Interpolator interpolator =
                    AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in);
            Random random = new Random();
            //  View view = root.getChildAt(i);
            convertView.setVisibility(View.VISIBLE);
            convertView.setAlpha(0.85f);
            float xOffset = random.nextFloat() * maxWidthOffset;
            if (random.nextBoolean()) {
                xOffset *= -1;
            }
            convertView.setTranslationX(xOffset);
            float yOffset = random.nextFloat() * maxHeightOffset;
            if (random.nextBoolean()) {
                yOffset *= -1;
            }
            convertView.setTranslationY(yOffset);

            // now animate them back into their natural position
            convertView.animate()
                    .translationY(0f)
                    .translationX(0f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(1000)
                    .start();
        }else {
            convertView.setVisibility(View.VISIBLE);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ButterKnife.bind(this,convertView);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)convertView);
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(context.getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)convertView);
        }

        tvCategoryTitle.setText(titles.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (position==0) {
                    Intent i = new Intent(context, StudentEngagment.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }*/
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,null);
                if (position==0) {
                    if(Build.VERSION.SDK_INT>=21) {
                        Intent i = new Intent(context, QuestionAnswerInstructor.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i, activityOptionsCompat.toBundle());
                    }else {
                        Intent i = new Intent(context, QuestionAnswerInstructor.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }

                else  if (position==1) {
                    if(Build.VERSION.SDK_INT>=21) {
                        Intent i = new Intent(context, InstructorReviewAct.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i, activityOptionsCompat.toBundle());
                    }else {
                        Intent i = new Intent(context, InstructorReviewAct.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }

                else  if (position==2) {
                    if((Build.VERSION.SDK_INT>=21)) {
                        Intent i = new Intent(context, AnnounceMentAct.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i, activityOptionsCompat.toBundle());
                    }else {
                        Intent i = new Intent(context, AnnounceMentAct.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }

                else  if (position==3) {
                    if((Build.VERSION.SDK_INT>=21)) {
                        Intent i = new Intent(context, AutomaticMessageAct.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i, activityOptionsCompat.toBundle());
                    }else {
                        Intent i = new Intent(context, AutomaticMessageAct.class);
                        i.putExtra("cid", courseId);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }

               /* else  if (position==5) {
                    Intent i = new Intent(context, ConversionAnalysisAct.class);
                    i.putExtra("cid",courseId);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }*/

            }
        });
        return convertView;
    }
}
