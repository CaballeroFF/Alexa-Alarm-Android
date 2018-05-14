package com.example.caballero.delighted;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabFourFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "TabFourFragment";

    private TextView timeViwer,txtValue;
    Handler customHandler = new Handler();
    private LinearLayout txtcontainer;

    long startTime=0L, timeInMilliseconds=0L, timeSwapBuff=0L,updateTime=0L;

    Runnable updateTimeThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMilliseconds;
            int secs= (int)(updateTime/1000);
            int mins = secs/60;
            secs%=60;
            int millisec = (int) (updateTime%1000);
            timeViwer.setText(""+String.format("%02d",mins)+":"+
                    String.format("%02d",secs)+":"+
                    String.format("%03d",millisec));
            customHandler.postDelayed(this,0);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab4_frag,container,false);

        timeViwer =  view.findViewById(R.id.stopwatchTimer);
        txtcontainer = view.findViewById(R.id.txtcontainer);

        Button btnLap = view.findViewById(R.id.lapBtn);
        Button btnPause = view.findViewById(R.id.pauseBtn);
        Button btnStart = view.findViewById(R.id.startBtn);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();

                customHandler.postDelayed(updateTimeThread, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwapBuff=timeInMilliseconds;
                customHandler.removeCallbacks(updateTimeThread);
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflaterTime = (LayoutInflater)getActivity().getBaseContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View addview = inflaterTime.inflate(R.layout.laptimes,null);
                txtValue = view.findViewById(R.id.lapsTime);
                txtValue.setText("hello");
//                txtcontainer.addView(addview);
            }
        });

        return view;
    }
}

