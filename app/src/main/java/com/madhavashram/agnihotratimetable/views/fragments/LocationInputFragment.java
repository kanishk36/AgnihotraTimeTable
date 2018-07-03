package com.madhavashram.agnihotratimetable.views.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.managers.LocationUpdateManager;
import com.madhavashram.agnihotratimetable.managers.NetworkManager;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;
import com.madhavashram.agnihotratimetable.views.AbstractActivity;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.madhavashram.agnihotratimetable.managers.LocationUpdateManager.ACCESS_COARSE_LOCATION_PERMISSION;
import static com.madhavashram.agnihotratimetable.managers.LocationUpdateManager.ACCESS_FINE_LOCATION_PERMISSION;
import static com.madhavashram.agnihotratimetable.managers.LocationUpdateManager.REQUEST_CHECK_SETTINGS;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationInputFragment extends Fragment implements DatePickerDialog.OnDateSetListener, LocationUpdateManager.LocationUpdateListener {

    private AbstractActivity mActivity;
    private AutoCompleteTextView mAutoCompleteTextView;
    private EditText mTxtLatitude, mTxtLongitude;
    private RadioGroup mDateRadioGroup,mSearchByRadioGroup;
    private AutoCompleteTextAdapter autoCompleteTextAdapter;
    private Filter cityFilter;

    private Calendar mCalendar;
    private ArrayList<String> allPlaces;
    private NetworkManager mNetworkManager;
    private double mLatitude, mLongitude;
    private LocationUpdateManager mLocationUpdateManager;

    private static final int PERMISSION_CODE = 1;

    public LocationInputFragment() {
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

        mLatitude = -1.0;
        mLongitude = -1.0;
        mCalendar = Calendar.getInstance();
        mNetworkManager = new NetworkManager();
        autoCompleteTextAdapter = new AutoCompleteTextAdapter(mActivity, R.layout.autocomplete_text_layout);
        initFilter();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLocationUpdateManager = new LocationUpdateManager(mActivity);
        mLocationUpdateManager.setLocationUpdateListener(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_input, container, false);

        mAutoCompleteTextView = view.findViewById(R.id.txtPlace);
        final View latngContainer = view.findViewById(R.id.latLngContainer);
        mTxtLatitude = view.findViewById(R.id.txtLatitude);
        mTxtLongitude = view.findViewById(R.id.txtLongitude);

        mDateRadioGroup = view.findViewById(R.id.dateRadioGroup);
        mSearchByRadioGroup = view.findViewById(R.id.searchByGrp);

        mAutoCompleteTextView.setAdapter(autoCompleteTextAdapter);

        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View textView, int pos,
                                    long arg3) {

                mTxtLatitude.setText("");
                mTxtLongitude.setText("");

                String selectedText = ((TextView) textView).getText().toString();
                mAutoCompleteTextView.setSelection(selectedText.length());

                hideSoftKeyboard();

                new GetLatLongFromCityTask().execute(selectedText);
            }
        });

        mDateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {

                    case R.id.btnCurrentYear:
                        mCalendar = Calendar.getInstance();
                        break;

                    default:
                        break;
                }
            }
        });

        view.findViewById(R.id.btnSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mSearchByRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {

                    case R.id.srchByPlace:
                        mAutoCompleteTextView.setVisibility(View.VISIBLE);
                        latngContainer.setVisibility(View.GONE);

                        break;

                    case R.id.srchByLoc:
                        mAutoCompleteTextView.setVisibility(View.GONE);
                        latngContainer.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });

        view.findViewById(R.id.btnGetTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearchByRadioGroup.getCheckedRadioButtonId() == R.id.srchByPlace) {

                    if(TextUtils.isEmpty(mAutoCompleteTextView.getText().toString().trim())) {
                        CommonUtils.showErrorDialog(mActivity, getString(R.string.enterplace));
                        return;
                    }

                } else if(mSearchByRadioGroup.getCheckedRadioButtonId() == R.id.srchByLoc) {

                    if( (TextUtils.isEmpty(mTxtLatitude.getText().toString().trim()) &&
                            !TextUtils.isEmpty(mTxtLongitude.getText().toString().trim()) ) ||
                            ( TextUtils.isEmpty(mTxtLatitude.getText().toString().trim()) &&
                                    TextUtils.isEmpty(mTxtLongitude.getText().toString().trim())) ) {

                        CommonUtils.showErrorDialog(mActivity, getString(R.string.txtLatitude));
                        return;

                    } else if(!TextUtils.isEmpty(mTxtLatitude.getText().toString().trim()) &&
                            TextUtils.isEmpty(mTxtLongitude.getText().toString().trim()))  {

                        CommonUtils.showErrorDialog(mActivity, getString(R.string.txtLongitude));
                        return;
                    }

                }

                new TimeTableGenerator().execute();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.location_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_location:

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(mActivity, ACCESS_FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) {

                        mLocationUpdateManager.startLocationUpdates();

                    } else {
                        requestPermissions(new String[]{ACCESS_FINE_LOCATION_PERMISSION, ACCESS_COARSE_LOCATION_PERMISSION}, PERMISSION_CODE);
                    }

                } else {
                    mLocationUpdateManager.startLocationUpdates();
                }

                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationUpdateManager.startLocationUpdates();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mLocationUpdateManager.startLocationUpdates();
                        break;

                    case Activity.RESULT_CANCELED:
                        mLocationUpdateManager.stopLocationUpdates();
                        break;
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
    }

    @Override
    public void onLocationUpdate(Location location) {
        if(location != null) {
            mLocationUpdateManager.stopLocationUpdates();

            mTxtLatitude.setText(String.valueOf(location.getLatitude()));
            mTxtLongitude.setText(String.valueOf(location.getLongitude()));

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            CommonUtils.showProgressDialog(mActivity, "Fetching...");

            String city = getCityFromLatLong();
            mAutoCompleteTextView.setText(city);
            CommonUtils.dismissProgressDialog(mActivity);
        }

    }

    private void initFilter() {
        cityFilter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mLatitude = -1.0;
                mLongitude = -1.0;

                if (results != null && results.count > 0) {
                    autoCompleteTextAdapter.notifyDataSetChanged();
                } else {
                    autoCompleteTextAdapter.notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    try {
                        allPlaces = mNetworkManager.getAllPlaces(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = allPlaces;
                        filterResults.count = allPlaces.size();

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        hideSoftKeyboard();
                        showAlertMessage(getString(R.string.search_place_internet_error_message));

                    } catch (Exception e) {
                        e.printStackTrace();

                        hideSoftKeyboard();
                        showAlertMessage(getString(R.string.search_place_general_error_message));

                    }
                }

                return filterResults;
            }
        };
    }

    private void hideSoftKeyboard() {
        try {

            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(mAutoCompleteTextView.getWindowToken(), 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCityFromLatLong() {
        String locationName = "";
        try {
            if(Geocoder.isPresent()) {
                Geocoder geoCoder = new Geocoder(mActivity);
                List<Address> addresses = geoCoder.getFromLocation(mLatitude, mLongitude, 1);
                locationName = addresses.get(0).getLocality();
            } else {
                locationName = mNetworkManager.getCityFromLatLong(""+mLatitude+","+mLongitude);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if(TextUtils.isEmpty(locationName)) {
                locationName = "Unknown";
            }
        }

        return locationName;
    }

    private ArrayList<Double> getLatLongFromCityName(String locationName) throws Exception {
        return mNetworkManager.getLocationOfCity(locationName);
    }

    private void showTimeTableFragment(Bundle bundle) {
        TimeTableViewPagerFragment fragment = new TimeTableViewPagerFragment();
        fragment.setArguments(bundle);

        mActivity.pushFragment(true, fragment, true);

    }

    private void showDatePickerDialog() {
        Calendar calendarNow = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                this, calendarNow.get(Calendar.YEAR), calendarNow.get(Calendar.MONTH),
                calendarNow.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDateRadioGroup.check(R.id.btnCurrentYear);
            }
        });

        datePickerDialog.show();
    }

    private void showAlertMessage(final String message) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showErrorDialog(mActivity, message);
            }
        });
    }

    private void showTimeDialogBox(ArrayList<ArrayList<ArrayList<String>>> timeArray, int month, int day, int year)
    {
        ArrayList<ArrayList<String>> monthArray = timeArray.get(month);
        ArrayList<String> dayArray = monthArray.get(day-1);

        Bundle bundle = new Bundle();
        bundle.putInt(CommonUtils.DAY_TAG, day);
        bundle.putInt(CommonUtils.MONTH_TAG, month+1);
        bundle.putInt(CommonUtils.YEAR_TAG, year);
        bundle.putSerializable(CommonUtils.TIME_TABLE_LIST_TAG, dayArray);

        TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
        timeDialogFragment.setArguments(bundle);

        mActivity.showDialogFragment(timeDialogFragment, CommonUtils.TIME_FRAGMENT_TAG);
    }

    private class AutoCompleteTextAdapter extends ArrayAdapter<String>
    {
        AutoCompleteTextAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return (allPlaces != null) ? allPlaces.size() : 0;
        }

        @Override
        public String getItem(int position) {
            return allPlaces.get(position);
        }

        @Override
        @NonNull
        public Filter getFilter() {
            return cityFilter;
        }
    }

    private class TimeTableGenerator extends AsyncTask<Void, Void, Void>
    {

        private boolean isLeapYear, isNextViewToBeOpened = true;
        private ArrayList<ArrayList<ArrayList<String>>> timeArray = new ArrayList<>();
        private int day,month,year, checkedRadioGrpId;
        private String locationName;

        TimeTableGenerator() {

            day = mCalendar.get(Calendar.DAY_OF_MONTH);
            month = mCalendar.get(Calendar.MONTH);
            year = mCalendar.get(Calendar.YEAR);

            isLeapYear = ((year % 4) == 0);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checkedRadioGrpId = mSearchByRadioGroup.getCheckedRadioButtonId();

            String message, longitude, latitude;

            locationName = mAutoCompleteTextView.getText().toString().trim();
            latitude = mTxtLatitude.getText().toString().trim();
            longitude = mTxtLongitude.getText().toString().trim();

            if(!TextUtils.isEmpty(latitude)) {
                mLatitude = Double.parseDouble(latitude);
            }

            if(!TextUtils.isEmpty(longitude)) {
                mLongitude = Double.parseDouble(longitude);
            }

            if(mLongitude > 0) {
                mLongitude = mLongitude * (-1);
            }

            if(mDateRadioGroup.getCheckedRadioButtonId() == R.id.btnSelect) {
                message = "Fetching Time...";
            } else {
                message = "Generating Time Table ...";
            }
            CommonUtils.showProgressDialog(mActivity, message);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(checkedRadioGrpId == R.id.srchByPlace) {

                if(mLatitude == -1.0 && mLongitude == -1.0) {
                    try {
                        ArrayList<Double> latLong = getLatLongFromCityName(locationName);

                        if(latLong.size() > 0) {
                            mLatitude = latLong.get(0);
                            mLongitude = latLong.get(1);

                            if(mLongitude > 0) {
                                mLongitude = mLongitude * (-1);
                            }
                            calculateSunriseSunset();

                        } else {
                            isNextViewToBeOpened = false;
                            showAlertMessage(getString(R.string.city_location_error_message));
                        }

                    } catch (Exception e) {
                        isNextViewToBeOpened = false;
                        showAlertMessage(getString(R.string.city_location_error_message));
                        e.printStackTrace();
                    }
                } else {
                    calculateSunriseSunset();
                }

            } else if(checkedRadioGrpId == R.id.srchByLoc) {

                if(TextUtils.isEmpty(locationName)) {
                    locationName = getCityFromLatLong();
                }
                calculateSunriseSunset();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            CommonUtils.dismissProgressDialog(mActivity);

            if(isNextViewToBeOpened) {
                if(mDateRadioGroup.getCheckedRadioButtonId() == R.id.btnSelect) {
                    showTimeDialogBox(timeArray, month, day, year);

                } else {
                    if(locationName.equals("Unknown")) {
                        locationName = "Agnihotra";
                    }

                    Bundle bundle = new Bundle();

                    bundle.putSerializable(CommonUtils.TIME_TABLE_LIST_TAG, timeArray);
                    bundle.putInt(CommonUtils.MONTH_TAG, month);
                    bundle.putString(CommonUtils.CITY_TAG, locationName);
                    bundle.putDouble(CommonUtils.LATITUDE_TAG, mLatitude);
                    bundle.putDouble(CommonUtils.LONGITUDE_TAG, mLongitude);

                    showTimeTableFragment(bundle);
                }
            }
        }

        private void calculateSunriseSunset() {

            double k = .0174532925, z = 5.5;
            double aa, bb, cc, dd;

            if(isLeapYear) {
                aa = 3.762;
                bb = 282.605;
                cc = 6.589;
                dd = 1;
            } else {
                aa = 3.276;
                bb = 282.611;
                cc = 6.621;
                dd = 2;
            }
            for(int m=1; m<13; m++)
            {
                int u = 0;
                switch(m){
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        u = 31;
                        break;

                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        u = 30;
                        break;

                    case 2:
                        if(isLeapYear){
                            u = 29;
                        } else {
                            u = 28;
                        }
                        break;
                }

                ArrayList<ArrayList<String>> monthTimes = new ArrayList<>();

                for(int i=1; i<=u; i++)
                {
                    ArrayList<String> dayTimes = new ArrayList<>();

                    for(int v=6; v<=18; v+=12)
                    {
                        double s = ((275 * m / 9)) - dd * (((m + 9) / 12)) + i - 30;
                        double y = mLongitude / 15;
                        s = s + (v + y) / 24;
                        double w = 0.9856 * s - aa;
                        double q = w + 1.916 * Math.sin(w * k) + 0.02 * Math.sin(2 * w* k) + bb;
                        q = (q / 360 - ((int)(q/360))) * 360;
                        double p = ((Math.atan(0.91746 * Math.tan(q * k))) / k) / 15;
                        if(q > 90) {
                            p = p + 12;
                        } else if(q > 270) {
                            p = p + 12;
                        }
                        q = 0.39782 * Math.sin(q * k);
                        double r = (-q * Math.sin(mLatitude * k)) / (Math.sqrt(1 - q*q) * Math.cos(mLatitude * k));
                        w = (-Math.atan(r / Math.sqrt(-r*r +1)) + 1.57079633) / k;
                        if(v == 6){
                            w = 360 - w;
                        }
                        w = w / 15;
                        double x = w + p - 0.06571 * s - cc;
                        if(x < 0) {
                            x = x + 24;
                        } else if(x > 24) {
                            x = x - 24;
                        }
                        double e = x + y + z;
                        while(e > 12) {
                            if(e >= 13) {
                                e = e - 12;
                            }
                        }
                        double t = (int)(e) + (e - (int)(e)) * 0.6 + 0.005;
                        if((t - (int)(t)) >= 0.6) {
                            t = t + 0.4;
                        }
                        double tt = (int)((t - (int)(t)) * 100);
                        Integer hours = (int)(t);
                        Integer min = (int)(tt);
                        String time = ""+hours+":"+((min.toString().length() > 1) ? min : "0"+min);
                        dayTimes.add(time);

                    }
                    monthTimes.add(dayTimes);
                }

                timeArray.add(monthTimes);
            }
        }

    }

    private class GetLatLongFromCityTask extends AsyncTask<String, Void, List<Double>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtils.showProgressDialog(mActivity, "Please wait ...");
        }

        @Override
        protected List<Double> doInBackground(String... strings) {
            List<Double> latLong = new ArrayList<>();
            try {
                latLong = getLatLongFromCityName(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return latLong;
        }

        @Override
        protected void onPostExecute(List<Double> latLong) {
            super.onPostExecute(latLong);
            CommonUtils.dismissProgressDialog(mActivity);

            if(latLong.size() > 0) {
                mTxtLatitude.setText(String.valueOf(latLong.get(0)));
                mTxtLongitude.setText(String.valueOf(latLong.get(1)));
                mLongitude = latLong.get(1);
            }
        }
    }

}
