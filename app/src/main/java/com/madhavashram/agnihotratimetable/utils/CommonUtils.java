package com.madhavashram.agnihotratimetable.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.madhavashram.agnihotratimetable.views.AbstractActivity;
import com.madhavashram.agnihotratimetable.views.fragments.ProgressDialogFragment;

/**
 * Created by kanishk on 28/10/17.
 */

public class CommonUtils {

    public static final String MONTH_TAG = "month";
    public static final String DAY_TAG = "day";
    public static final String YEAR_TAG = "year";
    public static final String MENU_OPTION_TAG = "selected_menu_option";
    public static final String TIME_TABLE_LIST_TAG = "timetableList";
    public static final String CITY_TAG = "city";
    public static final String LATITUDE_TAG = "latitude";
    public static final String LONGITUDE_TAG = "longitude";
    public static final String PROGRESS_MESSAGE_TAG = "progress_message";
    public static final String TIME_FRAGMENT_TAG = "dialog_fragment";
    public static final String PDF_FILE_TAG = "pdfFileURI";
    private static final String DIALOG_FRAGMENT_TAG = "dialog_fragment";



    private static AlertDialog mAlertdialog;
    private static ProgressDialogFragment mProgressDialogFragment;


    /**
     * returs the device screen size
     *
     * @return point
     */
    public static Point getScreenSize(WindowManager wm) {
        DisplayMetrics dm;
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        Point point = new Point(dm.widthPixels, dm.heightPixels);
        dm = null;

        return point;
    }

    public static void showErrorDialog(Context context, String message) {

        if(mAlertdialog == null) {

            mAlertdialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertdialog.dismiss();
                            mAlertdialog = null;
                        }
                    })
                    .setCancelable(false)
                    .create();
            mAlertdialog.show();
        }
    }

    public static void showProgressDialog(AbstractActivity activity, String message)
    {
        dismissProgressDialog(activity);

        Bundle bundle = new Bundle();
        bundle.putString(PROGRESS_MESSAGE_TAG, message);

        mProgressDialogFragment = new ProgressDialogFragment();
        mProgressDialogFragment.setArguments(bundle);

        activity.showDialogFragment(mProgressDialogFragment, DIALOG_FRAGMENT_TAG);
    }

    public static void dismissProgressDialog(AbstractActivity activity)
    {
        if(mProgressDialogFragment != null) {
            activity.dismissDialogFragment(mProgressDialogFragment);
            mProgressDialogFragment = null;
        }
    }
}