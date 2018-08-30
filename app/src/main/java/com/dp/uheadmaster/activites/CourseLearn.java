package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ExpandedAdapter;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.dialogs.WelcomeMessageDialog;
import com.dp.uheadmaster.fragments.AnnouncementsFragment;
import com.dp.uheadmaster.fragments.LecturesFrag;
import com.dp.uheadmaster.fragments.QuestionAnswerFragment;
import com.dp.uheadmaster.fragments.ResourcesFragment;
import com.dp.uheadmaster.interfaces.RefreshFragmentInterface;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 16/09/2017.
 */

public class CourseLearn extends AppCompatActivity implements RefreshFragmentInterface {
    private RecyclerView recyclerView;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private SharedPrefManager sharedPrefManager;
    public static boolean enrolled;
    Button btnStartCourse;
    ProgressDialog progressDialog;
    public static int courseID = -1;
    public static ArrayList<String>pdfs;
    public static boolean isCourseFinished=false;
    private LinearLayout linearLayout;
    private String imagePath;
    private ImageView simpleVideoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_parent_layout);
        pdfs=new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearLayout=(LinearLayout) findViewById(R.id.content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView search = (ImageView) toolbar.findViewById(R.id.iv_search);
        sharedPrefManager = new SharedPrefManager(this);
        search.setVisibility(View.GONE);
        if (getIntent().getExtras() != null) {
            enrolled = getIntent().getExtras().getBoolean("enrolled");
            courseID = getIntent().getExtras().getInt("course_id");
            imagePath=getIntent().getExtras().getString("IMAGE_PATH");
        }
      //  Toast.makeText(this, "Course Id :"+courseID, Snackbar.LENGTH_LONG ).show();

        initializeUi();
        initEventDriven();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent i=new Intent(getApplicationContext(),MainAct.class);
                startActivity(i);
                finish();
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initEventDriven() {
        btnStartCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrolledCourse();

            }
        });
    }

    private void enrolledCourse() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(CourseLearn.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest wishListRequest = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.courseEnrolled(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), wishListRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {


                            Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG ).show();
                            btnStartCourse.setVisibility(View.GONE);
                            if(getIntent().getStringExtra("WELCOME_MESSAGE")!=null && !getIntent().getStringExtra("WELCOME_MESSAGE").equals("") ) {
                                WelcomeMessageDialog welcomeMessageDialog = new WelcomeMessageDialog(CourseLearn.this, getIntent().getStringExtra("WELCOME_MESSAGE"),1,courseID);
                                welcomeMessageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                welcomeMessageDialog.setCancelable(true);
                                welcomeMessageDialog.show();
                            }
                            enrolled=true;
                           /* finish();
                            startActivity(getIntent());*/
                            viewPager.setAdapter(viewPagerAdapter);


//                            setTabs();

                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Snackbar.make(linearLayout, response.body().getMessage(), Snackbar.LENGTH_LONG ).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG ).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void initializeUi() {


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btnStartCourse = (Button) findViewById(R.id.btn_preview_course);
        simpleVideoView=(ImageView)findViewById(R.id.simpleVideoView);
        if(imagePath!=null && !imagePath.equals(""))
            Picasso.with(getApplicationContext()).load(imagePath).into(simpleVideoView);
        // viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),true);
        if (enrolled) {
            btnStartCourse.setVisibility(View.GONE);
        }
        setTabs();
    }

    private void setTabs() {
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), true);
        else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), false);


        viewPagerAdapter.addFragment(new LecturesFrag(), getString(R.string.lectures));
        viewPagerAdapter.addFragment(new QuestionAnswerFragment(), getString(R.string.questions_answers));
        viewPagerAdapter.addFragment(new AnnouncementsFragment(), getString(R.string.announcements));
        viewPagerAdapter.addFragment(new ResourcesFragment(), getString(R.string.resources));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

              /*  if (tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3) {
                    if (!enrolled) {
                        viewPager.setCurrentItem(0);
                      //  tabLayout.getTabAt(0).select();
                       // Snackbar.warning(CourseLearn.this, "You have to buy", Snackbar.LENGTH_LONG , true).show();
                    }
                }*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPager.setCurrentItem(0, false);
        else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPager.setCurrentItem(viewPagerAdapter.getCount() - 1, false);
        // viewPager.setRotationY(180);


        changeTabsFont();
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
                        ((TextView) tabViewChild).setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1"), Typeface.NORMAL);

                    if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
                        ((TextView) tabViewChild).setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"), Typeface.NORMAL);
                }
            }
        }
    }


    @Override
    public void onRefreshFragment() {
        //viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdfs=null;
        isCourseFinished=false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),MainAct.class);
        startActivity(i);
        finishAffinity();
    }
}
