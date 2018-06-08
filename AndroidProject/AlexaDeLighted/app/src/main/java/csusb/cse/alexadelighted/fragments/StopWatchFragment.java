package csusb.cse.alexadelighted.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import csusb.cse.alexadelighted.R;

public class StopWatchFragment extends Fragment {

    private static final String TAG = "DEBUG";

    private TextView timeViwer,txtValue;
    Handler customHandler = new Handler();

    String currentTime,currentTimeandLaps;

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
            currentTime = ""+String.format("%02d",mins)+":"+
                    String.format("%02d",secs)+":"+
                    String.format("%03d",millisec);
            timeViwer.setText(currentTime);
            customHandler.postDelayed(this,0);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: stopwatch");

        View rootView = inflater.inflate(R.layout.stop_watch_layout, container, false);
        timeViwer =  rootView.findViewById(R.id.stopwatchTimer);
        txtValue = rootView.findViewById(R.id.timeLaps);

        Log.d(TAG, "onCreateView: the laps worked");

        Button btnLap = rootView.findViewById(R.id.lapBtn);
        Button btnPause = rootView.findViewById(R.id.pauseBtn);
        Button btnStart = rootView.findViewById(R.id.startBtn);

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
                if (currentTime != null){
                    if (currentTimeandLaps == null){
                        txtValue.setText(currentTime);
                        currentTimeandLaps = currentTime;
                    }
                    else{
                        currentTimeandLaps = currentTimeandLaps + "\n" + currentTime;
                        txtValue.setText(currentTimeandLaps);
                    }
                }
            }
        });
        return rootView;
    }
}