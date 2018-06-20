package com.madhavashram.agnihotratimetable.views.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;

import java.util.List;

/**
 * Created by kanishk1 on 27/11/17.
 */

public class TimeDialogFragment extends BaseDialogFragment {

    private List<String> mDayArray;
    private String mDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        int dayOfMonth = args.getInt(CommonUtils.DAY_TAG);
        int month = args.getInt(CommonUtils.MONTH_TAG);
        int year = args.getInt(CommonUtils.YEAR_TAG);
        mDayArray = (List<String>) args.getSerializable(CommonUtils.TIME_TABLE_LIST_TAG);

        mDate = String.valueOf(dayOfMonth).concat("/").concat(String.valueOf(month)).concat("/").concat(String.valueOf(year));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dialogParentView = inflater.inflate(R.layout.fragment_time_dialog, null);

        int defaultDialogWidth = (int) getResources().getDimension(R.dimen.dp_309);

        Window window = getDialog().getWindow();
        int dialogWidth = getDialogWidth(window.getWindowManager());

        if(dialogWidth > defaultDialogWidth) {

            LinearLayout mainContainer = dialogParentView.findViewById(R.id.dialogMainContainer);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mainContainer.getLayoutParams();
            mainContainer.setLayoutParams(params);
        }

        dialogParentView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialogParentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.txtDate)).setText(mDate);
        ((TextView) view.findViewById(R.id.lblAMTime)).setText(mDayArray.get(0));
        ((TextView) view.findViewById(R.id.lblPMTime)).setText(mDayArray.get(1));
    }

    private int getDialogWidth(WindowManager wm) {
        Point screenSize = CommonUtils.getScreenSize(wm);

        int width = screenSize.x;
        int margins = (int) (getResources().getDimension(R.dimen.dp_20) * 2);

        return width - margins;
    }
}
