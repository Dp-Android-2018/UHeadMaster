package com.dp.uheadmaster.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.dialogs.DatePickerFragment;
import com.dp.uheadmaster.interfaces.DateValue;
import com.dp.uheadmaster.models.Comment;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.InstructorCourse;
import com.dp.uheadmaster.models.request.AddAnnouncementCommentRequest;
import com.dp.uheadmaster.models.request.AddAnnouncementRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 18/10/2017.
 */

public class AnnounceMentAct extends AppCompatActivity implements DateValue{

    @BindView(R.id.act_title)
    TextView tvactTitle;

    @BindView(R.id.tv_announcement_tile)
    TextView tvannouncementTitle;

    @BindView(R.id.et_anouncement_title)
    EditText etannouncementTitle;



    @BindView(R.id.tv_end_date_text)
    TextView tvEndDate;


    @BindView(R.id.btn_end_date)
    Button btnEndDate;

    @BindView(R.id.tv_announcement_content_txt)
    TextView tvContent;

    @BindView(R.id.et_announcement_data)
    EditText etAnnouncementData;

    @BindView(R.id.btn_publish)
    Button btnPublish;


    private ProgressDialog progressDialog;
    private int courseId;
    private SharedPrefManager sharedPrefManager;

    /////////////////////////////Add Announcement Comment //////////////////////

    /*private int announcementId=1;
    private String comment="";

    ////////////////////////////////////////////////////////////////////////////

    private int pageId=0;
    private String next_page=null;




    /////////////////////////////Instructor Courses ///////////////////
    private int  instructorId=1;*/
    //////////////////////////////////////////////////////////////
    public static WeakReference<DateValue> dateValue=null;
    private String mServerDate;
    private LinearLayout linearLayout;
    private FontChangeCrawler fontChanger;
    Slide slide;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         dateValue=new WeakReference<DateValue>(this);
       /* if(Build.VERSION.SDK_INT>21){
             slide=new Slide();
            slide.setDuration(2000);
            getWindow().setEnterTransition(slide);
        }*/
        setContentView(R.layout.act_instructor_announcement_layout);
        linearLayout=(LinearLayout)findViewById(R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        courseId=getIntent().getIntExtra("cid",0);
        //Toast.makeText(this, "Cid:"+courseId, Snackbar.LENGTH_LONG).show();
        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN) )
        {
            fontChanger = new FontChangeCrawler(getAssets(), "font/Roboto-Bold.ttf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
        }

        if (ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR) ) {
            fontChanger = new FontChangeCrawler(getAssets(), "font/GE_SS_Two_Medium.otf");
            fontChanger.replaceFonts((ViewGroup)findViewById(android.R.id.content));
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



    @OnClick(R.id.btn_publish)
    public void publishbtnaction(){
        if (!ConfigurationFile.isEmpty(etannouncementTitle)&&!ConfigurationFile.isEmpty(etAnnouncementData)){

            addAnnounceMent();

        }else {
            Snackbar.make(linearLayout, getString(R.string.fill_data), Snackbar.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_end_date)
    public void getDate(){
        DialogFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void addAnnounceMent(){
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(AnnounceMentAct.this);

            System.out.println("Announcement Data :"+etannouncementTitle.getText().toString().trim());
            System.out.println("Announcement Data :"+courseId);
            System.out.println("Announcement Data :"+etAnnouncementData.getText().toString().trim());
            System.out.println("Announcement Data :"+mServerDate);
            AddAnnouncementRequest addAnnouncementRequest=new AddAnnouncementRequest(etannouncementTitle.getText().toString().trim(),courseId,etAnnouncementData.getText().toString().trim(),mServerDate);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);




            Call<DefaultResponse> call = apiService.addAnnouncements(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), addAnnouncementRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        DefaultResponse defaultResponse=response.body();
                        if (defaultResponse.getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            Snackbar.make(linearLayout,getString(R.string.add_announcement), Snackbar.LENGTH_LONG).show();
                            /*Intent i=new Intent(AnnounceMentAct.this,InstructorCourseContent.class);
                            startActivity(i);*/
                        //    finish();
                                    new Handler().postDelayed(new RunnableHandler(),1000);

                        } else {
                            // parse the response body …
                            Snackbar.make(linearLayout,defaultResponse.getMessage(),Snackbar.LENGTH_LONG).show();
                            System.out.println("error Code message :" + defaultResponse.getMessage());


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

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(linearLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }





   /* public void addAnnounceMentComment(){
        if (NetWorkConnection.isConnectingToInternet(getApplicationContext(),linearLayout)) {
            progressDialog = ConfigurationFile.showDialog(AnnounceMentAct.this);

            AddAnnouncementCommentRequest addAnnouncementCommentRequest=new AddAnnouncementCommentRequest(announcementId,comment);
            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);


            Call<DefaultResponse> call = apiService.addAnnouncementComment(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), addAnnouncementCommentRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);

                    try {


                        DefaultResponse defaultResponse=response.body();
                        if (defaultResponse.getStatus() == 200) {
                            // use response data and do some fancy stuff :)
                            Snackbar.make(linearLayout,getString(R.string.add_comment), Snackbar.LENGTH_LONG).show();

                        } else {
                            // parse the response body …
                            System.out.println("error Code message :" + defaultResponse.getMessage());


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

                    Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println(" Fialer :" + t.getMessage());
                }
            });

        } else {
            Snackbar.make(linearLayout,getString(R.string.check_internet_connection),Snackbar.LENGTH_LONG).show();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String resultDate = bundle.getString("selectedDate", "error");
              //  start_date = bundle.getString("serverdate", "error");
               // et_startDate.setText(resultDate);
                //   Toast.makeText(getActivity().getApplicationContext(), " start Date"+start_date, Snackbar.LENGTH_LONG).show();

            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String resultDate = bundle.getString("selectedDate", "error");
            //    expire_date = bundle.getString("serverdate", "error");
            //    et_endDate.setText(resultDate);
                //   Toast.makeText(getActivity().getApplicationContext(), "end date"+expire_date, Snackbar.LENGTH_LONG).show();

            }
        }
    }*/


    @Override
    public void getDateValue(String date) {
        mServerDate=date;
        btnEndDate.setText(date);

    }
    public class RunnableHandler implements Runnable {

        @Override
        public void run() {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        slide=null;
    }
}




