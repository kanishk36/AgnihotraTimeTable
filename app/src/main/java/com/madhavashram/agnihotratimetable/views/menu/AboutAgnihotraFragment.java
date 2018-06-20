package com.madhavashram.agnihotratimetable.views.menu;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAgnihotraFragment extends Fragment {


    public AboutAgnihotraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Point screenSize = CommonUtils.getScreenSize(getActivity().getWindowManager());


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_agnihotra, container, false);

        ImageView imgAbtAgnihotra = view.findViewById(R.id.imgAbtAgnihotra);
        ImageView imgSahab = view.findViewById(R.id.imgSahab);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(screenSize.x * 0.5), (int)(screenSize.y * 0.3));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        imgAbtAgnihotra.setLayoutParams(params);
        imgSahab.setLayoutParams(params);

        return view;
    }

}
