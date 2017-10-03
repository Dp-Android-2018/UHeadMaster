package com.dp.uheadmaster.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 07/05/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private boolean mIsRtlOrientation;

    public ViewPagerAdapter(FragmentManager fm , boolean mIsRtlOrientation) {
        super(fm);
        this.mIsRtlOrientation=mIsRtlOrientation;

    }
    @Override
    public Fragment getItem(int position) {

        if (mIsRtlOrientation && mFragmentList != null && mFragmentList.size() > 0) {
            return mFragmentList.get(mFragmentList.size() - position - 1);
        } else {
            return mFragmentList.get(position);
        }


        //return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mIsRtlOrientation && mFragmentTitleList != null && mFragmentTitleList.size() > 0) {
            return mFragmentTitleList.get(mFragmentTitleList.size() - position - 1);
        } else {
            return mFragmentTitleList.get(position);
        }
        //return mFragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
