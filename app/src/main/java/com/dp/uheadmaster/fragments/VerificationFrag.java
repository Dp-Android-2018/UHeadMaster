package com.dp.uheadmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.MainAct;
import com.dp.uheadmaster.models.response.CourseResponse;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.NetWorkConnection;
import com.dp.uheadmaster.utilities.SharedPrefManager;
import com.dp.uheadmaster.webService.ApiClient;
import com.dp.uheadmaster.webService.EndPointInterfaces;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 28/05/2018.
 */

public class VerificationFrag extends Fragment {

    private SharedPrefManager sharedPrefManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.frag_mail_activation_layout,container,false);;
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        return  v;

    }

    @Override
    public void onResume() {
        super.onResume();
       /* if (!(getArguments()!=null && (getArguments().getInt("Verify")==1)) ){
            System.out.println("Verify Best Seller ");
                    getBestSellers();
        }*/
        getBestSellers();
    }

    public void getBestSellers() {
        if (NetWorkConnection.isConnectingToInternet(getActivity().getApplicationContext(),getActivity().findViewById(R.id.ll_container))) {
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
                        navigateToVerify();
                    }else {

                    }
                }

                @Override
                public void onFailure(Call<CourseResponse> call, Throwable t) {
                    Snackbar.make(getActivity().findViewById(R.id.content), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            // ConfigurationFile.hideDialog(progressDialog);
        }
    }

    public void navigateToVerify(){

        Intent i=new Intent(getActivity(),MainAct.class);
        getActivity().startActivity(i);
        getActivity().finish();
    }
}
