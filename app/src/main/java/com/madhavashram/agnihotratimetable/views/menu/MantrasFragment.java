package com.madhavashram.agnihotratimetable.views.menu;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MantrasFragment extends Fragment {

    private Typeface mKrutiDev;


    public MantrasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        mKrutiDev = Typeface.createFromAsset(activity.getAssets(), "fonts/KRDV010.TTF");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mantras, container, false);

        ((TextView) view.findViewById(R.id.txtSunsetHindi1)).setTypeface(mKrutiDev);
        ((TextView) view.findViewById(R.id.txtSunsetHindi2)).setTypeface(mKrutiDev);

        ((TextView) view.findViewById(R.id.txtSunriseHindi1)).setTypeface(mKrutiDev);
        ((TextView) view.findViewById(R.id.txtSunriseHindi2)).setTypeface(mKrutiDev);

        return view;
    }

}
