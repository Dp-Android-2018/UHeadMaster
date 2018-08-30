package com.dp.uheadmaster.fragments;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.models.FontChangeCrawler;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

/**
 * Created by DELL on 17/10/2017.
 */

public class InstructorFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private SharedPrefManager sharedPrefManager;
    private Activity maActivity;
    private FontChangeCrawler fontChanger;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        maActivity=activity;
    }

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

        viewPagerAdapter.addFragment(new RoadMapFragment(), getString(R.string.create_course));
        viewPagerAdapter.addFragment(new InstructorDashboard(), getString(R.string.dashboard));


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout=(TabLayout)v.findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dot_active_screen);
                        //tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.course_constructor_name);
                       // tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

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
