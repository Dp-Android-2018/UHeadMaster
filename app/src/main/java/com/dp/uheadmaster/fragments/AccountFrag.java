package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.ChangePasswordAct;
import com.dp.uheadmaster.activites.CourseDetailAct;
import com.dp.uheadmaster.interfaces.RefershWishList;
import com.dp.uheadmaster.models.CourseIDRequest;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.UpdateEmailRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by DELL on 27/08/2017.
 */

public class AccountFrag extends Fragment {
    private Button btnChangePassword, btnSave;
    private EditText etEmail;
    private String oldEMail = "";
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private FontChangeCrawler fontChanger;
    private View v;
    private Activity mHostActivity;
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
       v = inflater.inflate(R.layout.fragment_account_layout, container, false);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        initializeUi(v);
        setData();
        initEventDriven();
        return v;
    }

    private void initEventDriven() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePasswordAct.class);
                startActivity(i);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = "";
                eMail = etEmail.getText().toString();
                if (eMail .equals(oldEMail)) {
                    Snackbar.make(mHostActivity.findViewById(R.id.content), getString(R.string.the_old_email), Snackbar.LENGTH_LONG).show();
                } else if (eMail.equals("")) {
                    Snackbar.make(mHostActivity.findViewById(R.id.content), getString(R.string.email_null), Snackbar.LENGTH_LONG).show();
                } else {
                    updateEmail(eMail);
                }
            }
        });
    }

    private void updateEmail(String eMail) {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mHostActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest(eMail);
            Call<DefaultResponse> call = apiService.updateEmail(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), updateEmailRequest);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    try {

                        Snackbar.make(mHostActivity.findViewById(R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mHostActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("updateEmail / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void initializeUi(View v) {
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        btnChangePassword = (Button) v.findViewById(R.id.btn_reset_password);
        btnSave = (Button) v.findViewById(R.id.btn_save);
//        etUserName=(EditText)v.findViewById(R.id.et_user_name);
        etEmail = (EditText) v.findViewById(R.id.et_user_email);

    }

    public void setData() {
        // if (sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME) != null)
        //etUserName.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME));

        if (sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_EMAIL) != null) {
            etEmail.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_EMAIL));
            oldEMail = sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_EMAIL);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHostActivity=activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHostActivity=null;
    }
}
