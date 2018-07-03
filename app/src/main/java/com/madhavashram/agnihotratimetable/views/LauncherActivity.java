package com.madhavashram.agnihotratimetable.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;

public class LauncherActivity extends AbstractActivity {

    private boolean isActivityLaunched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        TextView textView = findViewById(R.id.lblTouchAnywhere);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein_infinite);
        textView.startAnimation(fadeInAnimation );

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isActivityLaunched) {
            isActivityLaunched = true;

            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.dispatchTouchEvent(ev);
    }
}
