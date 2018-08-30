package com.dp.uheadmaster.activites;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.CourseContentAdapter;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.InstructorCourse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 17/10/2017.
 */

public class InstructorCourseContent extends AppCompatActivity {

    @BindView(R.id.gridview_course_contents)
    GridView gvCourseContents;
    @BindView(R.id.tv_course_name)
    TextView tvCourseTitle;
    @BindView(R.id.tv_videos_num)
    TextView tvVideoNum;
    @BindView(R.id.tv_lecture_num)
    TextView tvLectureNum;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_price)
    TextView tvCoursePrice;


    @BindView(R.id.tv_students_num)
    TextView tvNumOfStudents;

    @BindView(R.id.iv_course)
    ImageView ivCourse;

    @BindView(R.id.rb_rating)
    RatingBar ratingBar;


    @BindView(R.id.content)
    LinearLayout linearLayout;
    private InstructorCourse instructorCourse;

    ArrayList<String> data = new ArrayList<>();
    private FontChangeCrawler fontChanger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if(Build.VERSION.SDK_INT>21){
            Transition transition= TransitionInflater.from(this).inflateTransition(R.transition.activity_transition);
            getWindow().setExitTransition(transition);
        }*/
        setContentView(R.layout.act_course_detail_instructor_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            instructorCourse = (InstructorCourse) getIntent().getSerializableExtra("course_obj");
        }
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        }


        setInitData();
        //data.add(getResources().getString(R.string.student_engagment));
        data.add(getString(R.string.question_answer));
        data.add(getString(R.string.reviews));
        data.add(getString(R.string.create_announcement));
        data.add(getString(R.string.automatic_messages));
        // data.add(getString(R.string.convertion_analysis));
        gvCourseContents.setAdapter(new CourseContentAdapter(InstructorCourseContent.this, data,instructorCourse.getId()));
        //animateViewsIn();
    }

    private void animateViewsIn() {
        // setup random initial state
        ViewGroup root = (ViewGroup) findViewById(R.id.gridview_course_contents);
        float maxWidthOffset = 2f * getResources().getDisplayMetrics().widthPixels;
        float maxHeightOffset = 2f * getResources().getDisplayMetrics().heightPixels;
        Interpolator interpolator =
                AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        Random random = new Random();
        int count = data.size();
        //   Toast.makeText(this, "Count :"+count, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < count; i++) {
            View view = root.getChildAt(i);
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.85f);
            float xOffset = random.nextFloat() * maxWidthOffset;
            if (random.nextBoolean()) {
                xOffset *= -1;
            }
            view.setTranslationX(xOffset);
            float yOffset = random.nextFloat() * maxHeightOffset;
            if (random.nextBoolean()) {
                yOffset *= -1;
            }
            view.setTranslationY(yOffset);

            // now animate them back into their natural position
            view.animate()
                    .translationY(0f)
                    .translationX(0f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(1000)
                    .start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setInitData() {

        if(instructorCourse != null){
            String status="";
            String price="";

            tvCourseTitle.setText(instructorCourse.getTitle());
            tvLectureNum.setText(instructorCourse.getNumOfLectures()+" "+getString(R.string.ilec));
            tvVideoNum.setText( instructorCourse.getNumOfvideos() +" "+getString(R.string.videos));
            if(instructorCourse.getStatus().equals("published")){
                status=getResources().getString(R.string.published);
            }else if(instructorCourse.getStatus().equals("draft")){
                status=getResources().getString(R.string.draft);
            }else if(instructorCourse.getStatus().equals("pending")){
                status=getResources().getString(R.string.pending);

            }else if(instructorCourse.getStatus().equals("rejected")){
                status=getResources().getString(R.string.rejected);

            }

            if(instructorCourse.getPrice()!=null){
                price=instructorCourse.getPrice();

            }else {
                price=getResources().getString(R.string.free);
            }
            tvStatus.setText(status);
            tvCoursePrice.setText(price);

            tvNumOfStudents.setText(instructorCourse.getNumOfStudents()+" "+getString(R.string.student));
            if(instructorCourse.getRate()!=null && !instructorCourse.getRate().equals(""))
                ratingBar.setRating(Float.parseFloat(instructorCourse.getRate()));
            if(instructorCourse.getImage()!=null && !instructorCourse.getImage().equals(""))
                Picasso.with(getApplicationContext()).load(instructorCourse.getImage()).into(ivCourse);
        }
    }

    /*public void containerClick(View view) {
        animateViewsIn();
    }*/
}
