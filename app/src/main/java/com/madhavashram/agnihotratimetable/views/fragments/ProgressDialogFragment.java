package com.madhavashram.agnihotratimetable.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;

/**
 * A simple {@link BaseDialogFragment} subclass.
 */
public class ProgressDialogFragment extends BaseDialogFragment {

    private String mProgressMessage;

    public ProgressDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mProgressMessage = args.getString(CommonUtils.PROGRESS_MESSAGE_TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress_dialog, container, false);

        TextView txtMessage = view.findViewById(R.id.progressMessage);
        txtMessage.setText(mProgressMessage);

        return view;
    }

}
