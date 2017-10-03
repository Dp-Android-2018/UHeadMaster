package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CoursesListAct;
import com.dp.uheadmaster.adapters.CoursesAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.models.CourseModel;
import com.dp.uheadmaster.models.response.CourseResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by DELL on 22/08/2017.
 */

public class LearningCoursesFrag extends Fragment {
    private RecyclerView recyclerFreeCourses, recyclerPaidCourses;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    TextView tvFreeMore, tvPaidMore,tvPaidCourses,tvFreeCourses;
    private ProgressDialog progressDialog;
    private static List<CourseModel> freeCoursesList = new ArrayList<>();
    private static List<CourseModel> paidCoursesList = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;
    CoursesAdapter freeCoursesAdapter, paidCoursesAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_learning_courses_layout, container, false);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        initializeUi(view);
        if (freeCoursesList.size() == 0) {
            getFreeCourses();
        } else {
            notifyFreeCoursesAdapter();

        }
        initEventDriven();

        return view;
    }

    public void initializeUi(View v) {
        recyclerFreeCourses = (RecyclerView) v.findViewById(R.id.recyler_free_courses);
        recyclerPaidCourses = (RecyclerView) v.findViewById(R.id.recyler_paid_courses);
        recyclerFreeCourses.setItemAnimator(new DefaultItemAnimator());
        recyclerPaidCourses.setItemAnimator(new DefaultItemAnimator());
        tvFreeMore = (TextView) view.findViewById(R.id.tv_free_more);
        tvPaidMore = (TextView) view.findViewById(R.id.tv_paid_more);
        tvFreeCourses = (TextView) view.findViewById(R.id.tv_free_courses);
        tvPaidCourses = (TextView) view.findViewById(R.id.tv_paid_courses);

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            tvFreeCourses.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvPaidCourses.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvFreeMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvPaidMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
        } else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tvFreeCourses.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvPaidCourses.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvFreeMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvPaidMore.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
        }
    }

    public void getFreeCourses() {
        if (NetWorkConnection.isConnectingToInternet(getContext())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getFreeCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),0);
            System.out.println("FreeCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("FreeCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseResponse CourseResponse = response.body();
                            // Toasty.success(getContext(), "Done! cat.Num = " + CourseResponse.getCoursesList().size(), Toast.LENGTH_LONG, true).show();
                            freeCoursesList = CourseResponse.getCoursesList();
                            notifyFreeCoursesAdapter();

                        } else {
                            // parse the response body …
                            System.out.println("FreeCourses /error Code message :" + response.body().getMessage());
                            Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                            getPaidCourses();
                        }


                    } catch (Exception ex) {
                        Toasty.error(getContext(), ex.getMessage(), Toast.LENGTH_LONG, true).show();
                        getPaidCourses();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("FreeCourses / Fialer :" + t.getMessage());
                    getPaidCourses();

                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void getPaidCourses() {
        if (NetWorkConnection.isConnectingToInternet(getContext())) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<CourseResponse> call = apiService.getPaidCourses(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),0);
            System.out.println("PaidCourses / id : " + sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            System.out.println("PaidCourses / authro : " + sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN));
            call.enqueue(new Callback<CourseResponse>() {
                @Override
                public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            CourseResponse CourseResponse = response.body();
                             paidCoursesList = CourseResponse.getCoursesList();
                            notifyPaidAdapter();

                        } else {
                            // parse the response body …
                            System.out.println("PaidCourses /error Code message :" + response.body().getMessage());
                            Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_LONG, true).show();
                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_LONG, true).show();
                    System.out.println("PaidCourses / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    private void notifyFreeCoursesAdapter() {
        System.out.println("FreeCourses / Num = " + freeCoursesList.size());
        freeCoursesAdapter = new CoursesAdapter(getContext(), freeCoursesList);
        recyclerFreeCourses.setAdapter(freeCoursesAdapter);

        if (paidCoursesList.size() > 0) {
            notifyPaidAdapter();
        } else {
            getPaidCourses();
        }


    }

    private void notifyPaidAdapter() {
        System.out.println("ExploreCourses / Num = " + paidCoursesList.size());
        paidCoursesAdapter = new CoursesAdapter(getContext(), paidCoursesList);
        recyclerPaidCourses.setAdapter(paidCoursesAdapter);

    }

    private void initEventDriven() {

        tvFreeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open more of best sellers of courses screen
                Intent intent = new Intent(getApplicationContext(), CoursesListAct.class);
                intent.putExtra("reqest_type", 4);
                intent.putExtra("sub_category_title", getApplicationContext().getResources().getString(R.string.free_courses));
                startActivity(intent);
            }
        });
        tvPaidMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open more of Explore of courses screen
                Intent intent = new Intent(getApplicationContext(), CoursesListAct.class);
                intent.putExtra("reqest_type", 5);
                intent.putExtra("sub_category_title", getApplicationContext().getResources().getString(R.string.paid_courses));
                startActivity(intent);
            }
        });
    }


}
