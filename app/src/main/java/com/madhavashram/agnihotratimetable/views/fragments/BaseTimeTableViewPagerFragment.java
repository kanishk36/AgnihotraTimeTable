package com.madhavashram.agnihotratimetable.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.widgets.PageIndicator;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kanishk1 on 1/29/18.
 */

public abstract class BaseTimeTableViewPagerFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private PageIndicator mPageIndicator;
    private ViewPager mViewPager;
    private ViewPagerAdapter pagerAdapter;

    private String[] mMonths;
    private int mCurrentMonth;
    protected ArrayList<ArrayList<ArrayList<String>>> timeArray;
    protected FragmentActivity mFragmentActivity;


    public BaseTimeTableViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMonths = getResources().getStringArray(R.array.array_months);
        Calendar calendar = Calendar.getInstance();
        mCurrentMonth = calendar.get(Calendar.MONTH);

        Bundle bundle = getArguments();
        timeArray = (ArrayList<ArrayList<ArrayList<String>>>) bundle.getSerializable(CommonUtils.TIME_TABLE_LIST_TAG);

        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        mViewPager = view.findViewById(R.id.viewPager);
        mPageIndicator = view.findViewById(R.id.pageIndicator);

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setCurrentItem(mCurrentMonth);

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPageIndicator.setSelectedIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putString(CommonUtils.MONTH_TAG, mMonths[position]);
            bundle.putSerializable(CommonUtils.TIME_TABLE_LIST_TAG, timeArray.get(position));

            TimeTableItemFragment fragment = new TimeTableItemFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return mMonths.length;
        }
    }

}
