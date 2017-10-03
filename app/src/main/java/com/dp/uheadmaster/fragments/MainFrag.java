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
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

import java.util.Locale;

/**
 * Created by DELL on 21/08/2017.
 */

public class MainFrag extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private SharedPrefManager sharedPrefManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home_layout,container,false);
        initializeUi(v);
        return v;
    }


    public void initializeUi(View v)
    {
        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        CartFrag cartFrag=new CartFrag();
        cartFrag.verify=(CheckOutDialogInterface) getActivity();

        viewPager=(ViewPager)v.findViewById(R.id.viewpager);
         if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
                viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),true);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
           viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),false);


        viewPagerAdapter.addFragment(new HomeFrag(), getResources().getString(R.string.home));
        viewPagerAdapter.addFragment(new CategoriesFrag(), getResources().getString(R.string.categories));
        viewPagerAdapter.addFragment(new MyCoursesFrag(), getResources().getString(R.string.my_courses));
        viewPagerAdapter.addFragment(new CartFrag(), getResources().getString(R.string.cart));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout=(TabLayout)v.findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);
          if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPager.setCurrentItem(0,false);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPager.setCurrentItem(viewPagerAdapter.getCount()-1,false);
       // viewPager.setRotationY(180);



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
