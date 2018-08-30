package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CoursesListAct;
import com.dp.uheadmaster.activites.MainAct;
import com.dp.uheadmaster.adapters.CoursesAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.CourseResponse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFrag extends Fragment {

    TextView tvBestMore, tvExploreMore,tvBestSeller,tvExploreCourses;
    RecyclerView recyclBestSellers, recyclExplore;
    private ProgressDialog progressDialog;
    private  List<CourseModel> bestSellerCoursesList = new ArrayList<>();
    private  List<CourseModel> exploreCoursesList = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;
    CoursesAdapter bestSellerCoursesAdapter, exploreCoursesAdapter;
    CourseResponse courseResponse2;
    View view;
    private BroadcastReceiver receiver;
    private FontChangeCrawler fontChanger;
    private AppCompatActivity mActivtiy;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup) this.getView());
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Home Frag On Create View");
        view = inflater.inflate(R.layout.frag_home, container, false);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        initView();
        try {
            // Toast.makeText(getApplicationContext(), "Status 2:" + courseResponse2.getStatus(), Toast.LENGTH_SHORT).show();
        }catch (NullPointerException ex){
            ex.getMessage();
        }
        initEventDriven();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Home Frag On rESUMe");
           getData();
    }

    public void getData(){
        if (bestSellerCoursesList.size() == 0) {
            bestSellerCoursesAdapter = new CoursesAdapter(getContext(), bestSellerCoursesList);
            recyclBestSellers.setAdapter(bestSellerCoursesAdapter);

            exploreCoursesAdapter = new CoursesAdapter(getContext(), exploreCoursesList);
            recyclExplore.setAdapter(exploreCoursesAdapter);
            getBestSellers();
            getExploreCourses();
        } else {
            notifyBestSellerAdapter();
            notifyExploreAdapter();
        }
    }

    public void getBestSellers() {
        if (NetWorkConnection.isConnectingToInternet(mActivtiy.getApplicationContext(),mActivtiy.findViewById(R.id.content))) {
            //progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getBestSellers(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),0);
            System.out.println("BestSellers / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("BestSellers / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {


                    if (response.code()!=405) {
                        try {
                            CourseResponse CourseResponse = response.body();
                            if (CourseResponse.getStatus() == 200) {
                                bestSellerCoursesList = CourseResponse.getCoursesList();
                                notifyBestSellerAdapter();
                            } else {
                                Snackbar.make(mActivtiy.findViewById(R.id.content), CourseResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            Snackbar.make(mActivtiy.findViewById(R.id.content), ex.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }else {
                        System.out.println("UnConfirmed");
                        navigateToVerify();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    Snackbar.make(mActivtiy.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            // ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void getExploreCourses() {
        if (NetWorkConnection.isConnectingToInternet(mActivtiy.getApplicationContext(),mActivtiy.findViewById(R.id.content))) {
           // progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getExploreCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),0);
            System.out.println("ExploreCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("ExploreCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    //   ConfigurationFile.hideDialog(progressDialog);
                    if (response.code() != 405) {
                        try {


                            if (response.body().getStatus() == 200) {
                                // use response data and do some fancy stuff :)
                                CourseResponse CourseResponse = response.body();
                                // Snackbar.success(getContext(), "Done! cat.Num = " + CourseResponse.getCoursesList().size(), Toast.LENGTH_LONG, true).show();
                                exploreCoursesList = CourseResponse.getCoursesList();
                                notifyExploreAdapter();

                            } else {
                                // parse the response body …
                                System.out.println("ExploreCourses /error Code message :" + response.body().getMessage());
                                Snackbar.make(mActivtiy.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            }

                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }else {
                        System.out.println("UnConfirmed");
                        navigateToVerify();
                    }

                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    //ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivtiy.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("ExploreCourses / Fialer :" + t.getMessage());
                }
            });

        } else {
            //ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyBestSellerAdapter() {
        System.out.println("ExploreCourses / Num = " + bestSellerCoursesList.size());
        bestSellerCoursesAdapter = new CoursesAdapter(getContext(), bestSellerCoursesList);
        recyclBestSellers.setAdapter(bestSellerCoursesAdapter);

       /* if (exploreCoursesList.size() == 0) {
            getExploreCourses();
        } else {
            getExploreCourses();
        }*/


    }

    private void notifyExploreAdapter() {
        System.out.println("ExploreCourses / Num = " + exploreCoursesList.size());
        exploreCoursesAdapter = new CoursesAdapter(getContext(), exploreCoursesList);
        recyclExplore.setAdapter(exploreCoursesAdapter);

    }

    private void initEventDriven() {

        tvBestMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open more of best sellers of courses screen
                Intent intent = new Intent(getApplicationContext(), CoursesListAct.class);
                intent.putExtra("reqest_type", 1);
                intent.putExtra("sub_category_title", getApplicationContext().getResources().getString(R.string.best_sellers));
                startActivity(intent);
            }
        });
        tvExploreMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open more of Explore of courses screen
                Intent intent = new Intent(getApplicationContext(), CoursesListAct.class);
                intent.putExtra("reqest_type", 2);
                intent.putExtra("sub_category_title", getApplicationContext().getResources().getString(R.string.explore_courses));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tvBestSeller = (TextView) view.findViewById(R.id.tv_best_seller);
        tvExploreCourses = (TextView) view.findViewById(R.id.tv_explore_courses);
        tvBestMore = (TextView) view.findViewById(R.id.tv_best_sellers_more);
        tvExploreMore = (TextView) view.findViewById(R.id.tv_explore_courses_more);
        recyclBestSellers = (RecyclerView) view.findViewById(R.id.recy_best_sellers);
        recyclExplore = (RecyclerView) view.findViewById(R.id.recy_explore_courses);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        recyclBestSellers.setLayoutManager(horizontalLayoutManagaer);
//        recyclExplore.setLayoutManager(horizontalLayoutManagaer);

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            tvBestSeller.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvExploreCourses.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvBestMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvExploreMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
        } else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tvBestSeller.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvExploreCourses.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvBestMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvExploreMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
        }

    }

    public void navigateToVerify(){

        android.support.v4.app.FragmentManager fragmentManager = mActivtiy.getSupportFragmentManager();
        fragmentManager.popBackStack();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VerificationFrag verificationFrag=new VerificationFrag();
        Bundle bundle=new Bundle();
        bundle.putInt("Verify",1);
        verificationFrag.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame,verificationFrag);
        fragmentTransaction.commit();
        ((MainAct)mActivtiy).hideItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivtiy=(AppCompatActivity) activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mActivtiy.unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
