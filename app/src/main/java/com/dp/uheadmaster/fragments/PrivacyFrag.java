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
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.request.UpdateEmailRequest;
import com.dp.uheadmaster.models.request.UpdatePrivacyRequest;
import com.dp.uheadmaster.models.response.DefaultResponse;
import com.dp.uheadmaster.models.response.LoginResponse;
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

public class PrivacyFrag extends Fragment {

    private CheckBox chBoxPrivacy;
    private Button btnSave;
    private ProgressDialog progressDialog;
    private SharedPrefManager sharedPrefManager;
    private FontChangeCrawler fontChanger;
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
         v = inflater.inflate(R.layout.fragment_privace_layout, container, false);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        chBoxPrivacy = (CheckBox) v.findViewById(R.id.ch_box_privcy);
        btnSave = (Button) v.findViewById(R.id.btn_privacy_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean privacy = chBoxPrivacy.isChecked();
                updatePrivacy(privacy);

            }
        });
        return v;
    }

    private void updatePrivacy(boolean privacy) {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),mHostActivity.findViewById(R.id.content))) {
            progressDialog = ConfigurationFile.showDialog(getActivity());

            final EndPointInterfaces apiService =
                    ApiClient.getClient().create(EndPointInterfaces.class);
            UpdatePrivacyRequest updatePrivacyRequest = new UpdatePrivacyRequest(privacy);
            Call<LoginResponse> call = apiService.updatePrivacy(ConfigurationFile.ConnectionUrls.HEAD_KEY, ConfigurationFile.GlobalVariables.APP_LANGAUGE, sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TOKEN), sharedPrefManager.getIntegerFromSharedPrederances(ConfigurationFile.ShardPref.USER_ID), updatePrivacyRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    ConfigurationFile.hideDialog(progressDialog);
                    LoginResponse resp = response.body();
                    try {
                        if (resp.getStatus() == 200) {
                            Snackbar.make(mHostActivity.findViewById(R.id.content), getString(R.string.done), Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(mHostActivity.findViewById(R.id.content), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                        }

                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    ConfigurationFile.hideDialog(progressDialog);

                    Snackbar.make(mHostActivity.findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                    System.out.println("updateEmail / Fialer :" + t.getMessage());
                }
            });

        } else {
            ConfigurationFile.hideDialog(progressDialog);
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
