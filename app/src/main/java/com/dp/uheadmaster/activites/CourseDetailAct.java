package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.dialogs.DeleteCartCourseDialog;
import com.dp.uheadmaster.dialogs.WelcomeMessageDialog;
import com.dp.uheadmaster.fragments.CourseDescriptionFragment;
import com.dp.uheadmaster.fragments.CourseInstructorFragment;
import com.dp.uheadmaster.fragments.CourseViewReviewFragment;
import com.dp.uheadmaster.fragments.CourseWriteReviewFragment;
import com.dp.uheadmaster.fragments.WishListFrag;
import com.dp.uheadmaster.interfaces.CourseDescriptionInterface;
import com.dp.uheadmaster.interfaces.OnInstructorValueChanged;
import com.dp.uheadmaster.interfaces.RefershWishList;
import com.dp.uheadmaster.jwplayer.JWPlayerViewExample;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.CourseNotificationData;
import com.dp.uheadmaster.models.MyApplication;
import com.dp.uheadmaster.models.response.CourseDetailsModel;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailAct extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imgCourseIntro, imgPlayVideo;
    private TextView tvCourseTitle, tvDuration, tvPirce, tvOldPrice, tvLecture, tvLangauge;
    private ImageView imgFav, imgCart;
    private int courseID = -1;
    private ProgressDialog progressDialog1,progressDialog2,progressDialog3,progressDialog4;
    private SharedPrefManager sharedPrefManager;
    private static int IsWishCourse = -1;
    private static int IsInCart = -1;
    public static int instructord_id = -1;
    private CourseDetailsModel courseDetailsModel;
    private Button btnPreview, btnBuyCourse;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean enrolled;
    private String videoPath="";
    private ImageView search;
    private String message;
    private LinearLayout linearLayout;
    private String welcomeMessage;
    CourseDescriptionFragment descriptionFragment;
    //private RotateAnimation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(getApplicationContext()).inflateTransition(R.transition.shared_element_transition));
        }
        setContentView(R.layout.course_detail_act_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearLayout=(LinearLayout)findViewById(R.id.content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getResources().getString(R.string.course_details));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         search = (ImageView) toolbar.findViewById(R.id.iv_search);
        search.setImageResource(R.drawable.ic_notifications_service);
        search.setVisibility(View.GONE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), CourseNotificationActivity.class);
                i.putExtra("Course_Id",courseID);
                startActivity(i);

            }
        });
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        initializeUi();

        if (getIntent().hasExtra("course_id")) {
            courseID = getIntent().getExtras().getInt("course_id");
            //  Toast.makeText(this, "Id:"+courseID, Snackbar.LENGTH_LONG).show();
            if (courseID != -1) {
                System.out.println("Course Details / course id : " + courseID);
                getCourseDetails(false);
            }
        }
        initEventDriven();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initEventDriven() {
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add/remove to widh list
                wishListRequest();
               /* imgFav.startAnimation(anim);
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       imgFav.setAnimation(null);

                   }
               },1000);*/
            }
        });
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add/remove to cart list
                AddOrRemoveToCart();
            }
        });
        btnBuyCourse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                enrolledCourse();
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CourseDetailAct.this, CourseLearn.class);
                // Toasty.success(getApplicationContext()," "+courseID, Snackbar.LENGTH_LONG).show();
                i.putExtra("course_id", courseID);
                i.putExtra("enrolled", enrolled);
                i.putExtra("Final_Message",message);
                i.putExtra("WELCOME_MESSAGE",welcomeMessage);
                i.putExtra("IMAGE_PATH",getIntent().getStringExtra("Image_Path"));
                startActivity(i);
            }
        });
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


    private void wishListRequest() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog1 = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest wishListRequest = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.addOrRemoveFromWishList(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), wishListRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                   ConfigurationFile.hideDialog(progressDialog1);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            if (CourseDetailAct.IsWishCourse == 1) {
                                CourseDetailAct.IsWishCourse = 0;
                                try {
                                    RefershWishList refershWishList = new WishListFrag();
                                    refershWishList.refershList(getApplicationContext());
                                } catch (Exception e) {
                                    System.out.println("Course details /interface : " + e.getMessage());
                                }
                            } else {
                                CourseDetailAct.IsWishCourse = 1;
                            }
                            changeFavIcon();

                           // Toasty.success(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                         //   Toasty.error(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog1);

                  //  Toasty.error(CourseDetailAct.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout,t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog1);
        }
    }

    public void initializeUi() {

        btnPreview = (Button) findViewById(R.id.btn_preview_course);
        btnBuyCourse = (Button) findViewById(R.id.btn_buy_course);
        imgCourseIntro = (ImageView) findViewById(R.id.img_course_intro);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();
        height=(width*9)/16;

        imgCourseIntro.getLayoutParams().width=width;
        imgCourseIntro.getLayoutParams().height=height;
        if(getIntent().getStringExtra("Image_Path")!=null && !getIntent().getStringExtra("Image_Path").equals("")) {
            Picasso.with(CourseDetailAct.this)
                    .load(getIntent().getStringExtra("Image_Path"))
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
                    .into(imgCourseIntro);
        }else {
            imgCourseIntro.setImageResource(R.drawable.ic_course_detail_default);
        }
        imgPlayVideo = (ImageView) findViewById(R.id.img_play_video);
        tvCourseTitle = (TextView) findViewById(R.id.tv_course_title);
        tvDuration = (TextView) findViewById(R.id.tv_hours_num);
        tvPirce = (TextView) findViewById(R.id.tv_price);
        tvLecture = (TextView) findViewById(R.id.tv_lecture_num);
        tvLangauge = (TextView) findViewById(R.id.tv_language);
     /*   anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(500);*/
        imgFav = (ImageView) findViewById(R.id.img_add_fav);

        imgCart = (ImageView) findViewById(R.id.img_add_cart);

        imgPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), JWPlayerViewExample.class);
                i.putExtra("Course_Video", videoPath);
                startActivity(i);
            }
        });
         descriptionFragment = new CourseDescriptionFragment();
        CourseInstructorFragment instructorFragment = new CourseInstructorFragment();
        CourseWriteReviewFragment writeReviewFragment = new CourseWriteReviewFragment();
        CourseViewReviewFragment courseViewReviewFragment = new CourseViewReviewFragment(getIntent().getExtras().getInt("course_id"));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),true);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), true);
        else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), false);

        viewPagerAdapter.addFragment(descriptionFragment, getString(R.string.about_course));
        viewPagerAdapter.addFragment(instructorFragment, getString(R.string.instructor));
        viewPagerAdapter.addFragment(courseViewReviewFragment, getString(R.string.reviews));
        //viewPagerAdapter.addFragment(writeReviewFragment, getString(R.string.write_review));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPager.setCurrentItem(0, false);
        else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPager.setCurrentItem(viewPagerAdapter.getCount() - 1, false);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            tvCourseTitle.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1"));
            tvDuration.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));
            tvPirce.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));
            tvLecture.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));
            tvLangauge.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));

        } else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)) {
            tvCourseTitle.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvDuration.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvPirce.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvLecture.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvLangauge.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
        }


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

    public void getCourseDetails(final boolean check) {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog2 = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseDetailsModel> call = apiService.getCourseDetails(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseID);
            System.out.println("Course Details / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("Course Details / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseDetailsModel>() {
                @Override
                public void onResponse(Call<CourseDetailsModel> call, Response<CourseDetailsModel> response) {
                    ConfigurationFile.hideDialog(progressDialog2);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            courseDetailsModel = response.body();
                            System.out.println(" Course Detail id : " + courseDetailsModel.getId());
                            //call instr. data
                            //Toasty.success(getApplicationContext()," "+courseDetailsModel.getId(),Snackbar.LENGTH_LONG).show();
                            sharedPrefManager.addIntegerToSharedPrederances("InstructorId", courseDetailsModel.getInstructor_id());
                        //    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                           // pref.
                            OnInstructorValueChanged obj = new CourseInstructorFragment();
                         //BY ME    obj.getInstructorValue(CourseDetailAct.this, courseDetailsModel.getInstructor_id());

                           /* OnInstructorValueChanged obj2 = new CourseViewReviewFragment();
                            obj2.getInstructorValue(CourseDetailAct.this, courseDetailsModel.getId());*/
                            sharedPrefManager.addIntegerToSharedPrederances("CourseId", courseDetailsModel.getId());

                          //  System.out.println("Welcome Mesage :"+courseDetailsModel.getWelcomeMessage());
                            if(courseDetailsModel.getWelcomeMessage()!=null && !courseDetailsModel.getWelcomeMessage().equals("") && check==true) {
                                WelcomeMessageDialog welcomeMessageDialog = new WelcomeMessageDialog(CourseDetailAct.this, courseDetailsModel.getWelcomeMessage(),1,-1);
                                welcomeMessageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                welcomeMessageDialog.setCancelable(true);
                                welcomeMessageDialog.show();
                            }
                            showData(courseDetailsModel);



                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                         //   Toasty.error(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        System.out.println("Ex 1 "+ex.getMessage());
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        System.out.println("Ex 2 "+ex.getMessage());
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseDetailsModel> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog2);

                    //Toasty.error(CourseDetailAct.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout,t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog2);
        }
    }

    private void showData(CourseDetailsModel courseDetailsModel) {
      //  Toast.makeText(this, "Desc :"+courseDetailsModel.getDescription(), Toast.LENGTH_LONG).show();
        enrolled = courseDetailsModel.isEnrolled();
        System.out.println(" Course Detail eNROLLD : " + enrolled);
        message=courseDetailsModel.getCongratsMessage();
        welcomeMessage=courseDetailsModel.getWelcomeMessage();
        if(courseDetailsModel.getVodeoPath()!=null && !courseDetailsModel.getVodeoPath().equals("")) {
            imgPlayVideo.setVisibility(View.VISIBLE);
            videoPath=courseDetailsModel.getVodeoPath();
        }
        //check if student is rooled or not
        if (courseDetailsModel.isEnrolled()) {

            btnBuyCourse.setText(getResources().getString(R.string.enrolled));
            btnBuyCourse.setEnabled(false);
            btnBuyCourse.setBackgroundColor(Color.GRAY);
            btnPreview.setVisibility(View.VISIBLE);
            btnPreview.setText(getString(R.string.continue_course));
            imgFav.setVisibility(View.GONE);
            imgCart.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);

        } else
            {
            btnBuyCourse.setText(getResources().getString(R.string.enroll_course));
            btnBuyCourse.setVisibility(View.VISIBLE);
            btnBuyCourse.setEnabled(true);
            btnBuyCourse.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btnPreview.setVisibility(View.VISIBLE);
            btnPreview.setText(getString(R.string.preview));
            imgFav.setVisibility(View.VISIBLE);
            imgCart.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
        }
        CourseDetailAct.IsWishCourse = courseDetailsModel.getIsWishCourse();
        CourseDetailAct.IsInCart = courseDetailsModel.getIsInCart();
        changeFavIcon();
        changeCartIcon();
        try {

            if (courseDetailsModel.getTitile() != null && !courseDetailsModel.getTitile().isEmpty()) {
                tvCourseTitle.setText(courseDetailsModel.getTitile());
            }
            if (courseDetailsModel.getCourseDuration() != null && !courseDetailsModel.getCourseDuration().isEmpty()) {
                tvDuration.setText(getString(R.string.hours) + courseDetailsModel.getCourseDuration());
            }
            if (courseDetailsModel.getLangauge() != null && !courseDetailsModel.getLangauge().isEmpty()) {
                tvLangauge.setText(getString(R.string.langauge) +" : "+ courseDetailsModel.getLangauge());
            }
            if (courseDetailsModel.getCurrency() != null && !courseDetailsModel.getCurrency().isEmpty()) {
                tvPirce.setText(getString(R.string.price) + courseDetailsModel.getPrice() + " " + courseDetailsModel.getCurrency());
                //    Toast.makeText(this, ""+, Snackbar.LENGTH_LONG).show();
            }
            if (courseDetailsModel.getNoOfLectures() != null && !courseDetailsModel.getNoOfLectures().isEmpty()) {
                tvLecture.setText(getString(R.string.lecture) + courseDetailsModel.getNoOfLectures() + " " + getString(R.string.lec));
            }
         /*   if (courseDetailsModel.getImagePath() == null || courseDetailsModel.getImagePath().isEmpty()) {
                imgCourseIntro.setImageResource(R.drawable.ic_course_detail_default);
            } else {
                Picasso.with(CourseDetailAct.this)
                        .load(courseDetailsModel.getImagePath())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(imgCourseIntro);
            }*/

            //fire the interface for show course.Desc
            String requirmentsString = "";
            CourseDescriptionInterface courseDescriptionInterface =descriptionFragment;
            for (String prerequest : courseDetailsModel.getPreRequisites()) {
                requirmentsString = requirmentsString + " , " + prerequest;
            }
            if(requirmentsString!=null && !requirmentsString.equals(""))
            requirmentsString = requirmentsString.substring(2, requirmentsString.length());
           // System.out.println("PreRequests Data :" + requirmentsString);
         //   System.out.println("PreRequests Data : 2" + courseDetailsModel.getDescription());
            courseDescriptionInterface.setCourseDescription(courseDetailsModel.getDescription(), requirmentsString);
            ((MyApplication)getApplicationContext()).setmDescription(courseDetailsModel.getDescription());
            ((MyApplication)getApplicationContext()).setmRequirements(requirmentsString);


        } catch (Exception e) {
            System.out.println("Coure Details error :" + e.getMessage());
        }


    }


    private void changeFavIcon() {
        if (CourseDetailAct.IsWishCourse == 1) {
            imgFav.setImageResource(R.drawable.ic_fav_on);
        } else {
            imgFav.setImageResource(R.drawable.ic_fav_off);

        }

    }

    private void changeCartIcon() {
        if (CourseDetailAct.IsInCart == 1) {
            imgCart.setImageResource(R.drawable.ic_add_cart_on);
        } else {
            imgCart.setImageResource(R.drawable.ic_add_cart_off);


        }

    }

    private void AddOrRemoveToCart() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {

           progressDialog3 = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest request = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.addOrRemoveFromCartList(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), request);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog3);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            if (CourseDetailAct.IsInCart == 1) {
                                CourseDetailAct.IsInCart = 0;
                            } else {
                                CourseDetailAct.IsInCart = 1;
                            }
                       //     Toasty.success(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                           // Snackbar.make(linearLayout,getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG).show();
                            changeCartIcon();

                        } else {
                            // parse the response body …
                            System.out.println("cart adapter      /error Code message :" + response.body().getMessage());
                           // Toasty.error(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog3);

                   // Toasty.error(CourseDetailAct.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout,t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("cart adapter / Fialer :" + t.getMessage());
                }
            });

        } else {
           ConfigurationFile.hideDialog(progressDialog3);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        sharedPrefManager.addIntegerToSharedPrederances("InstructorId", 0);
    }

    private void enrolledCourse() {
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog4 = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest wishListRequest = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.courseEnrolled(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), wishListRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                   ConfigurationFile.hideDialog(progressDialog4);
                    try {
                        if (response.body().getStatus() == 200) {


                       //     Toasty.success(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            search.setVisibility(View.VISIBLE);
                            getCourseDetails(true);

                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                           // Toasty.error(CourseDetailAct.this, response.body().getMessage(), Snackbar.LENGTH_LONG, true).show();
                            Snackbar.make(linearLayout,response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog4);

                   // Toasty.error(CourseDetailAct.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    Snackbar.make(linearLayout,t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog4);
        }
    }



}
