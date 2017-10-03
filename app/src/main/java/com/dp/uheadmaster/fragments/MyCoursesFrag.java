package com.dp.uheadmaster.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.utilities.ConfigurationFile;

import java.util.Locale;

/**
 * Created by DELL on 22/08/2017.
 */

public class MyCoursesFrag extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_courses_layout,container,false);
        initializeUi(v);
        return v;
    }

    public void initializeUi(View v)
    {


        viewPager=(ViewPager)v.findViewById(R.id.viewpager);
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),true);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),false);

        viewPagerAdapter.addFragment(new LearningCoursesFrag(),getString(R.string.learning_courses));
        viewPagerAdapter.addFragment(new WishListFrag(), getString(R.string.wish_list));


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout=(TabLayout)v.findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPager.setCurrentItem(0,false);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPager.setCurrentItem(viewPagerAdapter.getCount()-1,false);

        changeTabsFont();
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
                        ((TextView) tabViewChild).setTypeface( ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("en_font1"), Typeface.NORMAL);

                    if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
                        ((TextView) tabViewChild).setTypeface(ApplyCustomFont.getInstance(getActivity().getApplicationContext()).chooseFont("ar_font"), Typeface.NORMAL);
                }
            }
        }
    }
}
