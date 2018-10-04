package com.madhavashram.agnihotratimetable.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madhavashram.agnihotratimetable.R;
import com.madhavashram.agnihotratimetable.utils.CommonUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unchecked")
public class TimeTableItemFragment extends Fragment {

    private String month;
    private ArrayList<ArrayList<String>> monthTimeTable;
    private TimetableListAdapter mAdapter;
    private AppCompatActivity mActivity;

    public TimeTableItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        month = bundle.getString(CommonUtils.MONTH_TAG);
        monthTimeTable = (ArrayList<ArrayList<String>>) bundle.getSerializable(CommonUtils.TIME_TABLE_LIST_TAG);
        mAdapter = new TimetableListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.time_table_item_layout, container, false);

        ((TextView) view.findViewById(R.id.headerText)).setText(month);
        ((TextView) view.findViewById(R.id.listHeaderLayout).findViewById(R.id.lblDay)).setTextColor(ContextCompat.getColor(mActivity, R.color.turquoise_dark));
        ((TextView) view.findViewById(R.id.listHeaderLayout).findViewById(R.id.lblAM)).setTextColor(ContextCompat.getColor(mActivity, R.color.turquoise_dark));
        ((TextView) view.findViewById(R.id.listHeaderLayout).findViewById(R.id.lblPM)).setTextColor(ContextCompat.getColor(mActivity, R.color.turquoise_dark));

        RecyclerView recyclerView = view.findViewById(R.id.timeTableList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private class DayTimeViewHolder extends RecyclerView.ViewHolder {

        private TextView lblDay, lblAM, lblPM;

        DayTimeViewHolder(View itemView) {
            super(itemView);

            lblDay = itemView.findViewById(R.id.lblDay);
            lblAM = itemView.findViewById(R.id.lblAM);
            lblPM = itemView.findViewById(R.id.lblPM);
        }
    }

    private class TimetableListAdapter extends RecyclerView.Adapter<DayTimeViewHolder> {

        @Override
        public DayTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timetable_item, parent, false);
            return new DayTimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayTimeViewHolder holder, int position) {
            ArrayList<String> timeArray = monthTimeTable.get(position);

            holder.lblDay.setText(String.valueOf(position+1));
            holder.lblAM.setText(timeArray.get(0));
            holder.lblPM.setText(timeArray.get(1));
        }

        @Override
        public int getItemCount() {
            return monthTimeTable.size();
        }
    }
}
