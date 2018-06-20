package com.madhavashram.agnihotratimetable.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.madhavashram.agnihotratimetable.R;

/**
 * Created by kanishk1 on 03/11/17.
 */

public class AbstractActivity extends AppCompatActivity {

    protected int mWindowAnimation = R.style.SlideAnimation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = mWindowAnimation;
    }

    public void pushFragment(boolean addToHistory, Fragment fragment, boolean isAnimated) {
        try {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if(isAnimated) {
                transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left,R.anim.slide_to_right ,R.anim.slide_from_left);
            }

            transaction.replace(R.id.fragment_container, fragment);

            if(addToHistory) {
                transaction.addToBackStack(fragment.getClass().getName());
            }
            transaction.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showDialogFragment(Fragment dialogFragment, String tag) {
        try {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(dialogFragment, tag).commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dismissDialogFragment(Fragment dialogFragment) {
        try {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(dialogFragment).commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
