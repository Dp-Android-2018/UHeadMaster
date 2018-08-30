package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.SplashAct;
import com.dp.uheadmaster.dialogs.DeleteCartCourseDialog;
import com.dp.uheadmaster.dialogs.DeleteCourseDialog;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
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
 * Created by DELL on 08/04/2017.
 */

public class SettingsFragment extends Fragment {


    View parent;
    LinearLayout langaugeLayout;
    private LinearLayout llDeleteAccount;
    TextView tvLangauge;
    private SharedPrefManager sharedPrefManager;
    private FontChangeCrawler fontChanger;
    private Activity mActivity;
    private Context mContext;
    private ProgressDialog progressDialog;
    private SwitchCompat switchCompat;
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
        parent = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPrefManager =new SharedPrefManager(getActivity().getApplicationContext());
        initView();
        initEventDriven();
        return parent;
    }

    private void initView() {
        switchCompat=(SwitchCompat)parent.findViewById(R.id.sc_subscribe) ;

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchCompat.isChecked())
                    subScribe();
                else
                    unSubScribe();
            }
        });
        langaugeLayout = (LinearLayout) parent.findViewById(R.id.layout_langauge);
        llDeleteAccount=(LinearLayout)parent.findViewById(R.id.ll_delete_account);
        llDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCourseDialog deleteCourseDialog=new DeleteCourseDialog(mContext,mActivity);
                deleteCourseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteCourseDialog.show();
            }
        });
        tvLangauge = (TextView) parent.findViewById(R.id.tv_langaue);
    //    Toast.makeText(mActivity, "Checks :"+sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED), Toast.LENGTH_SHORT).show();
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED).equals("0"))
            switchCompat.setChecked(false);
        else
            switchCompat.setChecked(true);
    }

    private void initEventDriven() {


        langaugeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.message_confirm))
                        .setContentText(getResources().getString(R.string.change_langauge_message))
                        .setCancelText(getResources().getString(R.string.cancel_en))
                        .setConfirmText(getResources().getString(R.string.change))
                        .showCancelButton(true)

                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                ChangeLangaue();
                                sDialog.cancel();
                            }
                        })
                        .show();*/
                DeleteCartCourseDialog deleteCartCourseDialog=new DeleteCartCourseDialog(mContext,0,1);
                deleteCartCourseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteCartCourseDialog.show();
            }
        });

    }

    private void ChangeLangaue() {
       /* boolean langauge = prefUtil.getLanguage();
        if (langauge) { // Langaue is true == arabic
            prefUtil.setLanguage(false);
            prefUtil.setServerLanguage("en");

        } else {  // Langaue is flase == english
            prefUtil.setLanguage(true);
            prefUtil.setServerLanguage("ar");

        }*/
       if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.Langauge).equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
           sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.Langauge,ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR);
           ConfigurationFile.GlobalVariables.APP_LANGAUGE = ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR;
       }else {
           sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.Langauge,ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN);
           ConfigurationFile.GlobalVariables.APP_LANGAUGE = ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN;

       }

        startActivity(new Intent(getActivity(), SplashAct.class));


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }



    private void subScribe() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mActivity.findViewById(R.id.content))) {
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


                            Snackbar.make(mActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED,"1");


                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Snackbar.make(mActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            //  ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(mActivity.findViewById(R.id.content),getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }




    private void unSubScribe() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mActivity.findViewById(R.id.content))) {
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


                            Snackbar.make(mActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            sharedPrefManager.addStringToSharedPrederances(ConfigurationFile.ShardPref.IS_SUBSCRIBED,"0");


                        } else {
                            // parse the response body …
                            System.out.println("Course Details /error Code message :" + response.body().getMessage());
                            Snackbar.make(mActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("Course Details / Fialer :" + t.getMessage());
                }
            });

        } else {
            //  ConfigurationFile.hideDialog(progressDialog);
            Snackbar.make(mActivity.findViewById(R.id.content),getResources().getString(R.string.internet_message), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity=null;
        mContext=null;
    }
}