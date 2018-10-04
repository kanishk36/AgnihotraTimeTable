package com.madhavashram.agnihotratimetable.views.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Created by kanishk1 on 1/12/18.
 */

public class BaseDialogFragment extends DialogFragment {

    protected Dialog mDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        FragmentActivity mActivity = getActivity();

        FrameLayout root = new FrameLayout(mActivity);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(root);

        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }
}
