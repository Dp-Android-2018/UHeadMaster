package com.dp.uheadmaster.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

/**
 * Created by DELL on 27/08/2017.
 */

public class NotificationFrag extends Fragment {


    private FontChangeCrawler fontChanger;
    private TextView tvInstructor;
    private LinearLayout linearInstructor;
    private SharedPrefManager sharedPrefManager;

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
        View v=inflater.inflate(R.layout.frag_notification_layout,container,false);
        initializeUi(v);

        return v;
    }
    public void initializeUi(View v){
        sharedPrefManager=new SharedPrefManager(getActivity());
        tvInstructor=(TextView)v.findViewById(R.id.tv_instructor);
        linearInstructor=(LinearLayout)v.findViewById(R.id.linear_layout_instructor);
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_TYPE).equals("student")){
            tvInstructor.setVisibility(View.GONE);
            linearInstructor.setVisibility(View.GONE);

        }
    }
}
