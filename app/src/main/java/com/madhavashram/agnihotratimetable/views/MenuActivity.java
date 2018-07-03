package com.madhavashram.agnihotratimetable.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.views.menu.AboutAgnihotraFragment;
import com.madhavashram.agnihotratimetable.views.menu.AboutMadhavashramFragment;
import com.madhavashram.agnihotratimetable.views.menu.AgnihotraProcedureFragment;
import com.madhavashram.agnihotratimetable.views.menu.ContactUsFragment;
import com.madhavashram.agnihotratimetable.views.menu.GalleryFragment;
import com.madhavashram.agnihotratimetable.views.menu.LanguageSelectionFragment;
import com.madhavashram.agnihotratimetable.views.menu.MantrasFragment;

/**
 * Created by kanishk1 on 12/11/17.
 */

public class MenuActivity extends AbstractActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int position = getIntent().getIntExtra(CommonUtils.MENU_OPTION_TAG, 0);
        int title = -1;
        Fragment fragment = null;

        switch (position) {
            case 0: // About Agnihotra
                title = R.string.lstAbout_agnihotra;
                fragment = new AboutAgnihotraFragment();
                break;

            case 1: // Procedure
                title = R.string.lstProcedure;
                fragment = new AgnihotraProcedureFragment();
                break;

            case 2: // Mantras
                title = R.string.lstMantras;
                fragment = new MantrasFragment();
                break;

            case 3: // About Madhavashram
                title = R.string.lstAbout_madhavashram;
                fragment = new AboutMadhavashramFragment();
                break;

            case 4: // Gallery
                title = R.string.lstGallery;
                fragment = new GalleryFragment();
                break;

            case 5: // Language
                title = R.string.lstLanguage;
                fragment = new LanguageSelectionFragment();
                break;

            case 6: // Contact Us
                title = R.string.lstContact_us;
                fragment = new ContactUsFragment();
                break;
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.infoViewContainer, fragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }
}
