package com.madhavashram.agnihotratimetable.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Resources resources = getResources();
//        Configuration config = resources.getConfiguration();
//
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String defaultLang = getDeviceLanguage(config);
//        String userPrefLang = settings.getString("user_language", "en");
//
//        if(!defaultLang.equals(new Locale(userPrefLang).getLanguage())) {
//            setAppLocale(config, resources, userPrefLang);
//        }

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

    @SuppressWarnings("deprecation")
    private void setAppLocale(Configuration config, Resources resources, String lang) {
        Locale locale = new Locale(lang);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            createConfigurationContext(config);

        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }

}
