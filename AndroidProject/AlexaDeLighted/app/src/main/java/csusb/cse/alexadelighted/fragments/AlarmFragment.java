package csusb.cse.alexadelighted.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import csusb.cse.alexadelighted.R;
import csusb.cse.alexadelighted.adapters.RecyclerAdapter;

public class AlarmFragment extends Fragment {

    private static final String TAG = "DEBUG";

    private RecyclerView mAlarmRecycler;
    private ArrayList<String> mDataSet = new ArrayList<>();
    private View rootView;

    private String mURLString;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: AlarmFragment");

        rootView = inflater.inflate(R.layout.alarm_layout, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: Alarm Fragment");
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataSet = bundle.getStringArrayList("ALARM");
            mURLString = bundle.getString("ADDRESS");
            Log.d(TAG, "onStart: " + mDataSet);
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: data " + mDataSet);
        mAlarmRecycler = rootView.findViewById(R.id.alarm_layout_recycler_view);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getActivity(), mDataSet, mURLString);
        mAlarmRecycler.setAdapter(recyclerAdapter);
        mAlarmRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}