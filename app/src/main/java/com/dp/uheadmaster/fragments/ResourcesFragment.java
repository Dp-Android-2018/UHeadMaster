package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.activites.CourseLearn;
import com.dp.uheadmaster.adapters.QuestionAnswerAdapter;
import com.dp.uheadmaster.adapters.ResourcesAdapter;
import com.dp.uheadmaster.interfaces.OnItemClickInterface;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.models.Resource;
import com.dp.uheadmaster.utilities.ConfigurationFile;



/**
 * Created by DELL on 24/09/2017.
 */

public class ResourcesFragment extends Fragment {
    private RecyclerView recyclerQustions;
    private LinearLayoutManager mLayoutManager;
    private ImageView ivEmptyView;
    private FontChangeCrawler fontChanger;
    private Activity mActivity;

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

    @Override
    public void onPause() {
        super.onPause();
        //Toast.makeText(mActivity, "Finished", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_questions_answers_layout,container,false);
        recyclerQustions = (RecyclerView) v.findViewById(R.id.recycler1);
        ivEmptyView=(ImageView)v.findViewById(R.id.iv_resources_empty);

        try {


            if(CourseLearn.enrolled) {
                if (LecturesFrag.resources.size() == 0) {
                    recyclerQustions.setVisibility(View.GONE);
                    ivEmptyView.setVisibility(View.VISIBLE);
                    //ivEmptyView.setText(R.string.no_resources);
                } else {
                    recyclerQustions.setVisibility(View.VISIBLE);
                    ivEmptyView.setVisibility(View.GONE);
                }
                mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                recyclerQustions.setLayoutManager(mLayoutManager);
                recyclerQustions.setItemAnimator(new DefaultItemAnimator());
                recyclerQustions.setAdapter(new ResourcesAdapter(getActivity().getApplicationContext(), LecturesFrag.resources, new OnItemClickInterface() {
                    @Override
                    public void clickedItem(Resource resource) {
                        //  Snackbar.success(getActivity().getApplicationContext()," "+resource.getResourceId(), Toast.LENGTH_LONG).show();
                    }
                },mActivity));
            }else {
                recyclerQustions.setVisibility(View.GONE);

                ivEmptyView.setImageResource(R.drawable.ic_not_enrolled);
                ivEmptyView.setVisibility(View.VISIBLE);
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
}
