package com.madhavashram.agnihotratimetable.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.views.fragments.LocationInputFragment;

public class MainActivity extends AbstractActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private String menuArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.ad_mob_app_id));

        menuArray = getResources().getStringArray(R.array.menu_array);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        RecyclerView recyclerView = findViewById(R.id.left_drawer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DrawerMenuAdapter());

        setActionBar();

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        pushFragment(false, new LocationInputFragment(), false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(!closeDrawer()) {
            super.onBackPressed();
        }
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private boolean closeDrawer() {
        boolean isDrawerOpen = false;
        if(drawerLayout.isDrawerOpen(Gravity.START)) {
            isDrawerOpen = true;
            drawerLayout.closeDrawer(Gravity.START);

        }

        return isDrawerOpen;
    }

    private void startMenuActivity(final int selectedMenuOption) {
        closeDrawer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(CommonUtils.MENU_OPTION_TAG, selectedMenuOption);

                startActivity(intent);
            }
        }, 200);
    }

    private class DrawerMenuViewHolder extends RecyclerView.ViewHolder {

        TextView menuoption;

        DrawerMenuViewHolder(View itemView) {
            super(itemView);

            menuoption = itemView.findViewById(R.id.menuOption);
        }
    }

    private class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuViewHolder> {

        @Override
        public DrawerMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.drawerlistrow, null);
            return new DrawerMenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DrawerMenuViewHolder holder, int position) {
            holder.menuoption.setText(menuArray[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMenuActivity(holder.getAdapterPosition());
                }
            });

        }

        @Override
        public int getItemCount() {
            return menuArray.length;
        }
    }

}