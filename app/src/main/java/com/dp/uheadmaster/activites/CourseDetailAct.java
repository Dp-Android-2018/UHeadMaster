package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.fragments.CourseDescriptionFragment;
import com.dp.uheadmaster.fragments.CourseInstructorFragment;
import com.dp.uheadmaster.fragments.CourseViewReviewFragment;
import com.dp.uheadmaster.fragments.CourseWriteReviewFragment;
import com.dp.uheadmaster.fragments.WishListFrag;
import com.dp.uheadmaster.interfaces.CourseDescriptionInterface;
import com.dp.uheadmaster.interfaces.OnInstructorValueChanged;
import com.dp.uheadmaster.interfaces.RefershWishList;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.response.CourseDetailsModel;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.squareup.picasso.Picasso;

import java.util.Locale;

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
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private static int IsWishCourse = -1;
    private static int IsInCart = -1;
    public static int instructord_id = -1;
    private CourseDetailsModel courseDetailsModel;
    private Button preview,buyCourse;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_act_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView search=(ImageView)toolbar.findViewById(R.id.iv_search);
        search.setVisibility(View.GONE);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        if (getIntent().hasExtra("course_id")) {
            courseID = getIntent().getExtras().getInt("course_id");
            if (courseID != -1) {
                System.out.println("Course Details / course id : " + courseID);
                getCourseDetails();
            }
        }
        initializeUi();
        initEventDriven();

    }

    private void initEventDriven() {
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add/remove to widh list
                wishListRequest();
            }
        });
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add/remove to cart list
                AddOrRemoveToCart();
            }
        });
    }

    private void wishListRequest() {
        if (NetWorkConnection.isConnectingToInternet(CourseDetailAct.this)) {
            progressDialog = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest wishListRequest = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.addOrRemoveFromWishList(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), wishListRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            if (CourseDetailAct.IsWishCourse == 1) {
                                CourseDetailAct.IsWishCourse = 0;
                                try {
                                    RefershWishList refershWishList = new WishListFrag();
                                    refershWishList.refershList(CourseDetailAct.this);
                                } catch (Exception e) {
                                    System.out.println("Course details /interface : " + e.getMessage());
                                }
                            } else {
                                CourseDetailAct.IsWishCourse = 1;
                            }
                            changeFavIcon();

                            Toasty.success(CourseDetailAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();


                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Toasty.error(CourseDetailAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(CourseDetailAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void initializeUi() {

        preview=(Button)findViewById(R.id.btn_preview_course);
        imgCourseIntro = (ImageView) findViewById(R.id.img_course_intro);
        imgPlayVideo = (ImageView) findViewById(R.id.img_play_video);
        tvCourseTitle = (TextView) findViewById(R.id.tv_course_title);
        tvDuration = (TextView) findViewById(R.id.tv_hours_num);
        tvPirce = (TextView) findViewById(R.id.tv_price);
        tvLecture = (TextView) findViewById(R.id.tv_lecture_num);
        tvLangauge = (TextView) findViewById(R.id.tv_language);
        imgFav = (ImageView) findViewById(R.id.img_add_fav);
        imgCart = (ImageView) findViewById(R.id.img_add_cart);
        CourseDescriptionFragment descriptionFragment = new CourseDescriptionFragment();
        CourseInstructorFragment instructorFragment = new CourseInstructorFragment();
        CourseWriteReviewFragment writeReviewFragment = new CourseWriteReviewFragment();
        CourseViewReviewFragment courseViewReviewFragment = new CourseViewReviewFragment(courseID);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),true);

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) )
            viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),true);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
            viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),false);


        viewPagerAdapter.addFragment(descriptionFragment, getString(R.string.about_course));
        viewPagerAdapter.addFragment(instructorFragment, getString(R.string.instructor));
        viewPagerAdapter.addFragment(courseViewReviewFragment, getString(R.string.reviews));
        //viewPagerAdapter.addFragment(writeReviewFragment, getString(R.string.write_review));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPager.setCurrentItem(0,false);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPager.setCurrentItem(viewPagerAdapter.getCount()-1,false);

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) ) {
            tvCourseTitle.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1"));
            tvDuration.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));
            tvPirce.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));
            tvLecture.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));
            tvLangauge.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font2"));

        } else if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            tvCourseTitle.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvDuration.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvPirce.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvLecture.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
            tvLangauge.setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"));
        }
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(CourseDetailAct.this,CourseLearn.class);
               // Toasty.success(getApplicationContext()," "+courseID, Toast.LENGTH_LONG).show();
                i.putExtra("CourseId",courseID);
                startActivity(i);
            }
        });

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
                    if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
                        ((TextView) tabViewChild).setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1"), Typeface.NORMAL);

                    if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) )
                        ((TextView) tabViewChild).setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"), Typeface.NORMAL);
                }
            }
        }
    }

    public void getCourseDetails() {
        if (NetWorkConnection.isConnectingToInternet(CourseDetailAct.this)) {
            progressDialog = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseDetailsModel> call = apiService.getCourseDetails(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), courseID);
            System.out.println("Course Details / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("Course Details / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseDetailsModel>() {
                @Override
                public void onResponse(Call<CourseDetailsModel> call, Response<CourseDetailsModel> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            courseDetailsModel = response.body();
                            //call instr. data
                            //Toasty.success(getApplicationContext()," "+courseDetailsModel.getId(),Toast.LENGTH_LONG).show();
                            sharedPrefManager.addIntegerToSharedPrederances("InstructorId",courseDetailsModel.getInstructor_id());
                            OnInstructorValueChanged obj = new CourseInstructorFragment();
                            obj.getInstructorValue(CourseDetailAct.this, courseDetailsModel.getInstructor_id());

                           /* OnInstructorValueChanged obj2 = new CourseViewReviewFragment();
                            obj2.getInstructorValue(CourseDetailAct.this, courseDetailsModel.getId());*/
                           sharedPrefManager.addIntegerToSharedPrederances("CourseId",courseDetailsModel.getId());


                            showData(courseDetailsModel);

                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Toasty.error(CourseDetailAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseDetailsModel> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(CourseDetailAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void showData(CourseDetailsModel courseDetailsModel) {
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
                tvLangauge.setText(getString(R.string.langauge) + courseDetailsModel.getLangauge());
            }
            if (courseDetailsModel.getCurrency() != null && !courseDetailsModel.getCurrency().isEmpty()) {
                tvPirce.setText(getString(R.string.price) + courseDetailsModel.getPrice() + courseDetailsModel.getCurrency());
            }
            if (courseDetailsModel.getNoOfLectures() != null && !courseDetailsModel.getNoOfLectures().isEmpty()) {
                tvLecture.setText(getString(R.string.lecture) + courseDetailsModel.getNoOfLectures() + getString(R.string.lec));
            }
            if (courseDetailsModel.getImagePath() == null || courseDetailsModel.getImagePath().isEmpty()) {
                imgCourseIntro.setImageResource(R.drawable.ic_logo);
            } else {
                Picasso.with(CourseDetailAct.this)
                        .load(courseDetailsModel.getImagePath())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(imgCourseIntro);
            }

            //fire the interface for show course.Desc
            String requirmentsString = "";
            CourseDescriptionInterface courseDescriptionInterface = new CourseDescriptionFragment();
            for (int i = 0; i < courseDetailsModel.getPreRequisites().size(); i++) {
                if (i == 0) {
                    requirmentsString = courseDetailsModel.getPreRequisites().get(i);
                    break;
                }
                requirmentsString = requirmentsString + " , " + courseDetailsModel.getPreRequisites().get(i);
            }
            courseDescriptionInterface.setCourseDescription(courseDetailsModel.getDescription(), requirmentsString);
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
        if (NetWorkConnection.isConnectingToInternet(CourseDetailAct.this)) {

            progressDialog = ConfigurationFile.showDialog(CourseDetailAct.this);

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            CourseIDRequest request = new CourseIDRequest(courseID);
            Call<DefaultResponse> call = apiService.addOrRemoveFromCartList(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), request);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            if (CourseDetailAct.IsInCart == 1) {
                                CourseDetailAct.IsInCart = 0;
                            } else {
                                CourseDetailAct.IsInCart = 1;
                            }
                            Toasty.success(CourseDetailAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                            changeCartIcon();

                        } else {
                            // parse the response body …
                            System.out.println("cart adapter      /error Code message :" + response.body().getMessage());
                            Toasty.error(CourseDetailAct.this, response.body().getMessage(), Toast.LENGTH_LONG, true).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(CourseDetailAct.this, t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("cart adapter / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPrefManager sharedPrefManager=new SharedPrefManager(getApplicationContext());
        sharedPrefManager.addIntegerToSharedPrederances("InstructorId",0);
    }
}
