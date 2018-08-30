package com.dp.uheadmaster.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.CourseDescriptionInterface;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.MyApplication;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.Locale;

/**
 * Created by لا اله الا الله on 23/08/2017.
 */
public class CourseDescriptionFragment extends Fragment implements CourseDescriptionInterface {
    private  TextView tvRequirements , tvDesc,tvDescText;
    View view;

    private FontChangeCrawler fontChanger;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_course_description_layout,container,false);
       // Toast.makeText(getActivity().getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
        tvDesc = (TextView) view.findViewById(R.id.tv_description);
        tvDescText=(TextView)view.findViewById(R.id.tv_description_text);
        tvRequirements = (TextView) view.findViewById(R.id.tv_requirment);
       

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
            tvDesc.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            tvDescText.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvRequirements.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            

        }else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tvDesc.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvDescText.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvRequirements.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));

        }

     //   Toast.makeText(getActivity().getApplicationContext(), ""+((MyApplication)getActivity().getApplicationContext()).getmDescription()+"\n"+((MyApplication)getActivity().getApplicationContext()).getmRequirements(), Toast.LENGTH_SHORT).show();
        if(((MyApplication)getActivity().getApplicationContext()).getmDescription()!=null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                tvDesc.setText(Html.fromHtml(((MyApplication) getActivity().getApplicationContext()).getmDescription()));
            else
                tvDesc.setText(Html.fromHtml(((MyApplication) getActivity().getApplicationContext()).getmDescription(),Html.FROM_HTML_MODE_LEGACY));
        }
        if(((MyApplication)getActivity().getApplicationContext()).getmRequirements()!=null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                tvRequirements.setText(Html.fromHtml(((MyApplication) getActivity().getApplicationContext()).getmRequirements()));
            else
                tvRequirements.setText(Html.fromHtml(((MyApplication) getActivity().getApplicationContext()).getmRequirements(), Html.FROM_HTML_MODE_LEGACY));
        }

        return view;
    }


    @Override
    public void setCourseDescription(String desc, String requirements) {
      // System.out.println("Course desc Fragment : ");
       //
        //
        // Toast.makeText(getActivity().getApplicationContext(), "DESK:"+desc +"\n Requirements :"+requirements, Toast.LENGTH_LONG).show();
        if (desc != null && !desc.isEmpty()) {
            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N)
                tvDesc.setText(Html.fromHtml(desc));
            else
                 tvDesc.setText(Html.fromHtml(desc,Html.FROM_HTML_MODE_LEGACY));
            //}
         }
        if (requirements != null && !requirements.isEmpty()) {
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N)
                 tvRequirements.setText(Html.fromHtml(requirements));
            else
                tvRequirements.setText(Html.fromHtml(requirements,Html.FROM_HTML_MODE_LEGACY));
        }

    }



}
