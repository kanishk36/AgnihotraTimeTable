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
public class AgnihotraProcedureFragment extends Fragment {


    public AgnihotraProcedureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Point screenSize = CommonUtils.getScreenSize(getActivity().getWindowManager());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agnihotra_procedure, container, false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(screenSize.x * 0.5), (int)(screenSize.y * 0.3));
        params.setMargins(0, (int) getResources().getDimension(R.dimen.dp_20), 0, 0);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        ImageView imageView1 = view.findViewById(R.id.procedureImage1);
        imageView1.setLayoutParams(params);

        ImageView imageView2 = view.findViewById(R.id.procedureImage2);
        imageView2.setLayoutParams(params);

        ImageView imageView3 = view.findViewById(R.id.procedureImage3);
        imageView3.setLayoutParams(params);

        ImageView imageView4 = view.findViewById(R.id.procedureImage4);
        imageView4.setLayoutParams(params);

        ImageView imageView5 = view.findViewById(R.id.procedureImage5);
        imageView5.setLayoutParams(params);

        return view;
    }

}
