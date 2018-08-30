package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.request.SubScribeToCourse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.GetCourseNotification;
import com.dp.uheadmaster.models.response.SearchCoursesResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;
import com.google.gson.Gson;


import java.util.logging.Logger;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 02/01/2018.
 */


public class StudentCourseNotification extends Fragment{

    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private  int CourseId;
    private boolean question;
    private boolean announcement;
    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initializeUi();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void initializeUi(){
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
    }
  /*  public void setCourseNotification(){

        try {


            if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext())) {
                EndPointInterfaces apiServices = ApiClient.getClient().create(EndPointInterfaces.class);
                SubScribeToCourse subScribeToCourse = new SubScribeToCourse(CourseId, announcement, question);
                Call<DefaultResponse> call = apiServices.setCourseNotification(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), subScribeToCourse);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        DefaultResponse defaultResponse = response.body();
                        if (defaultResponse.getStatus() == 200) {
                            Toast.makeText(getActivity().getApplicationContext(), "" + defaultResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });
            } else {

                Toasty.info(getActivity().getApplicationContext(), getResources().getString(R.string.internet_message), Toast.LENGTH_LONG).show();
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    public void getCourseNotification(){

        try {


            if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext())) {
                EndPointInterfaces apiServices = ApiClient.getClient().create(EndPointInterfaces.class);
                SubScribeToCourse subScribeToCourse = new SubScribeToCourse(CourseId, announcement, question);
                Call<GetCourseNotification> call = apiServices.getCourseNotification(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),CourseId);
                call.enqueue(new Callback<GetCourseNotification>() {
                    @Override
                    public void onResponse(Call<GetCourseNotification> call, Response<GetCourseNotification> response) {
                        GetCourseNotification getCourseNotification = response.body();
                        if (getCourseNotification.getStatus() == 200) {
                            if(getCourseNotification.getData().getAnnouncement()==1)    announcement=true;
                            else announcement=false;


                            if(getCourseNotification.getData().getQuestion()==1)    question=true;
                            else question=false;
                        }

                    }

                    @Override
                    public void onFailure(Call<GetCourseNotification> call, Throwable t) {

                    }
                });
            } else {
                Toasty.info(getActivity().getApplicationContext(), getResources().getString(R.string.internet_message), Toast.LENGTH_LONG).show();
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }*/
}
