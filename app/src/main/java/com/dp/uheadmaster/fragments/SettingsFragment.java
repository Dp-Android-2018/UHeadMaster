package com.dp.uheadmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.SplashAct;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by DELL on 08/04/2017.
 */

public class SettingsFragment extends Fragment {


    View parent;
    LinearLayout langaugeLayout;
    TextView tvLangauge;
    private SharedPrefManager sharedPrefManager;

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

        langaugeLayout = (LinearLayout) parent.findViewById(R.id.layout_langauge);
        tvLangauge = (TextView) parent.findViewById(R.id.tv_langaue);

    }

    private void initEventDriven() {


        langaugeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
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
                        .show();
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
}