package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 27/08/2017.
 */

public class SubScribeFrag extends Fragment {

    private FontChangeCrawler fontChanger;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private Button btnSubScribe;
    private CheckBox mCheckSubScribe;
    private Activity mHostActivity;
    View v;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.frag_subscribe_layout,container,false);

        initializeUi(v);
        return v;
    }


    public void initializeUi(View v){
        btnSubScribe=(Button)v.findViewById(R.id.btn_subscribe);
        mCheckSubScribe=(CheckBox)v.findViewById(R.id.cb_subscribe);
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED).equals("0"))
                mCheckSubScribe.setChecked(false);
        else
        mCheckSubScribe.setChecked(true);


        btnSubScribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckSubScribe.isChecked())     subScribe();
                else unSubScribe();
            }
        });
    }
    private void subScribe() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mHostActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<DefaultResponse> call = apiService.subScribe(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {


                            Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED,"1");


                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(v, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
          //  ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(v,getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }




    private void unSubScribe() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mHostActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            Call<DefaultResponse> call = apiService.unsubScribe(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID));
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {
                        if (response.body().getStatus() == 200) {


                            Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED,"0");


                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(v, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            //  ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(v,getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHostActivity=activity;
    }
}
