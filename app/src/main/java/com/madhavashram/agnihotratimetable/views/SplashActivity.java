package com.madhavashram.agnihotratimetable.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();

        String defaultLang = getDeviceLanguage(config);
        String userPrefLang = CommonUtils.getUserPreferedLanguage(this);

        if(userPrefLang == null) {

            String[] supportedLang = resources.getStringArray(R.array.suported_languages);
            boolean isLangSupported = false;

            for(String lang : supportedLang)
            {
                if(lang.equals(defaultLang)) {
                    isLangSupported = true;
                    break;
                }
            }

            if(isLangSupported) {
                userPrefLang = defaultLang;
            } else {
                userPrefLang = getString(R.string.default_language);
            }
        }

        CommonUtils.setAppLocale(config, this, resources, userPrefLang);

        Intent intent = new Intent(SplashActivity.this, LauncherActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("deprecation")
    private String getDeviceLanguage(Configuration config) {
        String defaultLang;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            defaultLang = config.getLocales().get(0).getLanguage();

        } else {
            defaultLang = config.locale.getLanguage();
        }

        return defaultLang;
    }

}
