package com.madhavashram.agnihotratimetable.views.menu;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.views.AbstractActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LanguageSelectionFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private String userPrefLang;
    private AbstractActivity mActivity;

    public LanguageSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AbstractActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPrefLang = CommonUtils.getUserPreferedLanguage(mActivity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language_selection, container, false);

        RadioButton btnEnglish = view.findViewById(R.id.btnEnglish);
        RadioButton btnHindi = view.findViewById(R.id.btnHindi);

        if(userPrefLang.equals(getString(R.string.english_code))) {
            btnEnglish.setChecked(true);
        } else if(userPrefLang.equals(getString(R.string.hindi_code))) {
            btnHindi.setChecked(true);
        }

        RadioGroup langGrp = view.findViewById(R.id.languageSelectonGroup);
        langGrp.setOnCheckedChangeListener(this);

        return view;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String langCode = "";

        switch (checkedId) {
            case R.id.btnEnglish:
                langCode = getString(R.string.english_code);
                break;

            case R.id.btnHindi:
                langCode = getString(R.string.hindi_code);
                break;
        }

        if(!TextUtils.isEmpty(langCode)) {
            Resources resources = getResources();
            CommonUtils.setAppLocale(resources.getConfiguration(), mActivity, resources, langCode);
        }

        mActivity.recreate();
    }
}
