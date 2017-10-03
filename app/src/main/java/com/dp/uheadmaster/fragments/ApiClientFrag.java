package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

/**
 * Created by DELL on 27/08/2017.
 */

public class ApiClientFrag extends Fragment {
    private EditText etUserName;
    private SharedPrefManager sharedPrefManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_api_client_layout,container,false);
        initializeUi(v);
        setDataToUi();
        return v;
    }
    public void initializeUi(View v){
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        etUserName=(EditText)v.findViewById(R.id.et_user_name);
    }

    public void setDataToUi(){
        if(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME)!=null)
            etUserName.setText(sharedPrefManager.getStringFromSharedPrederances(ConfigurationFile.ShardPref.USER_NAME));
    }
}
