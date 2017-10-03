package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.CourseDescriptionInterface;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.Locale;

/**
 * Created by لا اله الا الله on 23/08/2017.
 */
public class CourseDescriptionFragment extends Fragment implements CourseDescriptionInterface {
    private static TextView tvRequirements , tvDesc,tvDescText;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_course_description_layout,container,false);
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

        return view;
    }


    @Override
    public void setCourseDescription(String desc, String requirements) {
        System.out.println("Course desc : "+desc +" , requirments : "+requirements);
        if (desc != null && !desc.isEmpty()) {
             if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvDesc.setText(Html.fromHtml(desc,Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvDesc.setText(Html.fromHtml(desc));
            }
         }
        if (requirements != null && !requirements.isEmpty()) {
            tvRequirements.setText(requirements);
        }
    }
}
