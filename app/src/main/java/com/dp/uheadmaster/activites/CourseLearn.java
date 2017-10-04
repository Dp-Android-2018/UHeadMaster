package com.dp.uheadmaster.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dp.uheadmaster.R;
import com.dp.uheadmaster.adapters.ExpandedAdapter;
import com.dp.uheadmaster.adapters.ViewPagerAdapter;
import com.dp.uheadmaster.customFont.ApplyCustomFont;
import com.dp.uheadmaster.fragments.AnnouncementsFragment;
import com.dp.uheadmaster.fragments.CartFrag;
import com.dp.uheadmaster.fragments.CategoriesFrag;
import com.dp.uheadmaster.fragments.HomeFrag;
import com.dp.uheadmaster.fragments.LecturesFrag;
import com.dp.uheadmaster.fragments.MyCoursesFrag;
import com.dp.uheadmaster.fragments.QuestionAnswerFragment;
import com.dp.uheadmaster.fragments.ResourcesFragment;
import com.dp.uheadmaster.interfaces.ParentPositionChecker;
import com.dp.uheadmaster.interfaces.RefreshFragmentInterface;
import com.dp.uheadmaster.interfaces.VideoPathChecker;
import com.dp.uheadmaster.models.TitleChild;
import com.dp.uheadmaster.models.TitleParent;
import com.dp.uheadmaster.utilities.ConfigurationFile;
import com.dp.uheadmaster.utilities.SharedPrefManager;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by DELL on 16/09/2017.
 */

public class CourseLearn extends AppCompatActivity implements RefreshFragmentInterface{
    private RecyclerView recyclerView;
    private static WebView mWebView;
    private static ExpandedAdapter expandedAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_parent_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView search=(ImageView)toolbar.findViewById(R.id.iv_search);
        search.setVisibility(View.GONE);
       initializeUi();

    }
    public void initializeUi(){


        viewPager=(ViewPager)findViewById(R.id.viewpager);
       // viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),true);

        if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
            viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),true);
        else if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_EN))
            viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),false);




        viewPagerAdapter.addFragment(new LecturesFrag(),getString(R.string.lectures));
        viewPagerAdapter.addFragment(new QuestionAnswerFragment(),getString(R.string.questions_answers));
        viewPagerAdapter.addFragment(new AnnouncementsFragment(), getString(R.string.announcements));
        viewPagerAdapter.addFragment(new ResourcesFragment(),getString(R.string.resources));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);

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
                        ((TextView) tabViewChild).setTypeface( ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("en_font1"), Typeface.NORMAL);

                    if(ConfigurationFile.GlobalVariables.APP_LANGAUGE.equals(ConfigurationFile.GlobalVariables.APP_LANGAUGE_AR))
                        ((TextView) tabViewChild).setTypeface(ApplyCustomFont.getInstance(getApplicationContext()).chooseFont("ar_font"), Typeface.NORMAL);
                }
            }
        }
    }


    @Override
    public void onRefreshFragment() {
        viewPagerAdapter.notifyDataSetChanged();
    }
}
