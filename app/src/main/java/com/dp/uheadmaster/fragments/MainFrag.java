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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.interfaces.CheckOutDialogInterface;
import com.dp.uheadmaster.models.FontChangeCrawler;
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
    private FontChangeCrawler fontChanger;
    public static int position=-1;

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
    private int[] tabIcons = {
            R.mipmap.home1,
            R.mipmap.black_shop_tag,
            R.mipmap.online_course,
            R.mipmap.shopping_cart4

    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home_layout,container,false);
        initializeUi(v);
        setupTabIcons();
       // int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dot_active_screen);
       /// tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

            int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dot_active_screen);
            tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);


    }

    public void initializeUi(View v)
    {
        CartFrag cartFrag=new CartFrag();
        cartFrag.verify=(CheckOutDialogInterface) getActivity();

        viewPager=(ViewPager)v.findViewById(R.id.viewpager);
       //
        //
        // Toast.makeText(getActivity().getApplicationContext(), " "+ConfigurationFile.GlobalVariables.APP_LANGAUGE, Toast.LENGTH_SHORT).show();
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
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        position=tab.getPosition();
                        int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.dot_active_screen);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.course_constructor_name);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
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


       // resizeTabLayout();


        changeTabsFont();
    }


    /*private  void resizeTabLayout(){
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(  tabLayout.getTabAt(0).getCustomView().getWidth()>=displayMetrics.widthPixels){
                    tabLayout.setTabMode(TabLayout.MODE_FIXED);
                }else {
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
            }
        });
        ;
    }*/


    @Override
    public void onStop() {
        super.onStop();
        for (int i=0;i<tabLayout.getTabCount();i++){
            int tabIconColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.course_constructor_name);
            tabLayout.getTabAt(i).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setupTabIcons() {
        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN)) {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        }

        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR)){
            tabLayout.getTabAt(0).setIcon(tabIcons[3]);
            tabLayout.getTabAt(1).setIcon(tabIcons[2]);
            tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            tabLayout.getTabAt(3).setIcon(tabIcons[0]);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        tabLayout=null;
        viewPager=null;
        viewPagerAdapter=null;
        fontChanger=null;

    }
}
