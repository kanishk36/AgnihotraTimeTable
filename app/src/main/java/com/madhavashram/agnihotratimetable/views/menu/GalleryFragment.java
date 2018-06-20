package com.madhavashram.agnihotratimetable.views.menu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.widgets.PageIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements ViewPager.OnPageChangeListener {


    private int[] imageResIds;
    private String[] imageNames;

    private GalleryAdapter pagerAdapter;
    private PageIndicator mPageIndicator;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageResIds = new int[]{R.drawable.m1, R.drawable.m4, R.drawable.m11, R.drawable.m12, R.drawable.m3, R.drawable.m8,
                R.drawable.m9, R.drawable.m10, R.drawable.m2};

        imageNames = getResources().getStringArray(R.array.imageName_array);

        pagerAdapter = new GalleryAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mPageIndicator = view.findViewById(R.id.galleryPageIndicator);
        mPageIndicator.setNumberOfIndicators(imageResIds.length);

        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

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

    private class GalleryAdapter extends FragmentStatePagerAdapter
    {

        GalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();

            bundle.putInt(GalleryContentFragment.IMAGE_RESID, imageResIds[position]);
            bundle.putString(GalleryContentFragment.IMAGE_NAME, imageNames[position]);

            GalleryContentFragment fragment = new GalleryContentFragment();

            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return imageResIds.length;
        }

    }

}
