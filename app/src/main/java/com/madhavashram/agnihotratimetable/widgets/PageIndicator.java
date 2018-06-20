package com.madhavashram.agnihotratimetable.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.madhavashram.agnihotratimetable.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kanishk1 on 09/11/17.
 */

public class PageIndicator extends LinearLayout {

    private List<ImageView> mIndicators;
    private int mNumberOfIndicators;

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setNumberOfIndicators(int indicators) {
        mNumberOfIndicators = indicators;
        addIndicators();
    }

    public void setSelectedIndicator(int position) {
        for(int i=0; i<mNumberOfIndicators; i++) {
            ImageView imageView = mIndicators.get(i);

            if(imageView.isSelected() && i != position) {
                imageView.setSelected(false);

            } else if(!imageView.isSelected() && i == position) {
                imageView.setSelected(true);
            }
        }
    }

    private void init(Context context, AttributeSet attrs) {

        mIndicators = new ArrayList<>();

        int padding = (int) getResources().getDimension(R.dimen.dp_10);

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator);

            mNumberOfIndicators = typedArray.getInteger(R.styleable.PageIndicator_indicators, 0);

            typedArray.recycle();
        }

        addIndicators();

        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.TRANSPARENT);
        setPadding(padding, padding, padding, padding);

    }

    private void addIndicators() {

        mIndicators.clear();
        removeAllViews();

        if(mNumberOfIndicators > 0) {

            int padding = (int) getResources().getDimension(R.dimen.dp_10);

            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, padding, 0);

            for(int i=0; i<mNumberOfIndicators; i++) {


                ImageView indicator = new ImageView(getContext());
                indicator.setLayoutParams(params);
                indicator.setSelected(false);
                indicator.setBackgroundResource(R.drawable.page_indicator);

                if(i == 0) {
                    indicator.setSelected(true);
                }

                addView(indicator);

                mIndicators.add(indicator);
            }
        }
    }

}
