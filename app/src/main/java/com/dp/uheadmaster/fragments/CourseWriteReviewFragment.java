package com.dp.uheadmaster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.Locale;

/**
 * Created by لا اله الا الله on 23/08/2017.
 */
public class CourseWriteReviewFragment extends Fragment {
    private TextView tvFname,tvLname,tvContent,tvProvider,tvInstructor;
    private EditText etFname,etLname,etComment;
    private Button btnPost,btnCancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_review_layout, container, false);
        initializeUi(v);
        return v;
    }
    public void initializeUi(View v){
        tvFname=(TextView)v.findViewById(R.id.tv_fname);
        tvLname=(TextView)v.findViewById(R.id.tv_lname);
        tvContent=(TextView)v.findViewById(R.id.tv_content);
        tvProvider=(TextView)v.findViewById(R.id.tv_provider);
        tvInstructor=(TextView)v.findViewById(R.id.tv_instructor);

        etFname=(EditText)v.findViewById(R.id.et_fname);
        etLname=(EditText)v.findViewById(R.id.et_lname);
        etComment=(EditText)v.findViewById(R.id.et_comment);

        btnPost=(Button)v.findViewById(R.id.btn_post);
        btnCancel=(Button)v.findViewById(R.id.btn_cancel);

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)){
            tvFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvContent.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvProvider.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            tvInstructor.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            etFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            etLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font2"));
            etComment.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            btnPost.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));
            btnCancel.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"));

        }else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tvFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvContent.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvProvider.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            tvInstructor.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            etFname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            etLname.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            etComment.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            btnPost.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
            btnCancel.setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"));
        }
    }
}
