package com.dp.uheadmaster.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.ChangePasswordRequest;
import com.dp.uheadmaster.models.request.SubScribeToCourse;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.GetCourseNotification;
import com.dp.uheadmaster.models.response.LoginResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 09/01/2018.
 */

public class CourseNotificationActivity extends AppCompatActivity {
    private FontChangeCrawler fontChanger;
    private CheckBox chQuestions;
    private CheckBox cbAnnouncements;
    private Button save;
    private int courseId;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notifications);
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        }


        initializeUi();
    }

    public void initializeUi(){
        sharedPrefManager=new SharedPrefManager(this);
        relativeLayout=(RelativeLayout)findViewById(R.id.content); 
        chQuestions=(CheckBox)findViewById(R.id.cb_question_notification);
        cbAnnouncements=(CheckBox)findViewById(R.id.cb_announcement_notification);
        save=(Button)findViewById(R.id.btn_save);
        courseId=getIntent().getIntExtra("Course_Id",0);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotificationStatus();
            }
        });

        getNotificationStatus();
    }

    public void getNotificationStatus(){

       // Toast.makeText(this, " "+sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), Toast.LENGTH_SHORT).show();
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),relativeLayout)) {
            progressDialog = ConfigurationFile.showDialog(CourseNotificationActivity.this);

          //  ChangePasswordRequest changePasswordRequest=new ChangePasswordRequest(etOldPassword.getText().toString().trim(),etNewPassword.getText().toString().trim(),etNewPassword.getText().toString().trim());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<GetCourseNotification> call = apiService.getCourseNotification(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),courseId);
            call.enqueue(new Callback<GetCourseNotification>() {
                @Override
                public void onResponse(Call<GetCourseNotification> call, Response<GetCourseNotification> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {

                            if(response.body().getData().getQuestion()==1)
                                chQuestions.setChecked(true);
                            else
                                chQuestions.setChecked(false);

                            if(response.body().getData().getAnnouncement()==1)
                                cbAnnouncements.setChecked(true);
                            else
                                cbAnnouncements.setChecked(false);
                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + response.body().getMessage());
                            Snackbar.make(relativeLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetCourseNotification> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                   // Snackbar.error(ChangePasswordAct.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(relativeLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }

    public void setNotificationStatus(){

        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),relativeLayout)) {
            progressDialog = ConfigurationFile.showDialog(CourseNotificationActivity.this);

            //  ChangePasswordRequest changePasswordRequest=new ChangePasswordRequest(etOldPassword.getText().toString().trim(),etNewPassword.getText().toString().trim(),etNewPassword.getText().toString().trim());
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            SubScribeToCourse subScribeToCourse=new SubScribeToCourse(courseId,cbAnnouncements.isChecked(),chQuestions.isChecked());

            Call<DefaultResponse> call = apiService.setCourseNotification(ConfigurationFile.ConnectionUrls.HEAD_KEY,ConfigurationFile.GlobalVariables.APP_LANGAUGE,sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN),sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID),subScribeToCourse);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        if (response.body().getStatus() == 200) {

                                    Snackbar.make(relativeLayout,getString(R.string.notifications_updated),Snackbar.LENGTH_LONG).show();
                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + response.body().getMessage());
                            Snackbar.make(relativeLayout, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    }catch (NullPointerException ex){
                        ex.printStackTrace();

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    // Snackbar.error(ChangePasswordAct.this, t.getMessage(), Snackbar.LENGTH_LONG, true).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(relativeLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }


    }
}
