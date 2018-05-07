package com.example.caballero.delighted;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;

public class TabTwoFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "TabTwoFragment";

    Time mTime;
    Handler handler;
    Runnable r;

    @Nullable
    @Override
    public drawingView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_frag,container,false);

        mTime = new Time();

        r = new Runnable() {
            @Override
            public void run() {
                mTime.setToNow();
                drawingView dv = new drawingView(getActivity(),
                        mTime.hour,mTime.minute,mTime.second,mTime.weekDay,mTime.monthDay, getBatteryLevel());
                handler.postDelayed(r,1000);
            }
        };
        handler = new Handler();

        mTime.setToNow();
        drawingView dv = new drawingView(getActivity(),
                mTime.hour,mTime.minute,mTime.second,mTime.weekDay,mTime.monthDay, getBatteryLevel());
        handler.postDelayed(r, 1000);

        return dv;

//        return view;
    }


    public float getBatteryLevel(){
        Intent batterIntent = getActivity().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int Level = batterIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batterIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (Level == -1 || scale == -1){
            return 50.0f;
        }

        return ((float) Level/ (float) scale) * 100.0f;
    }

    public class drawingView extends View {

        Paint mBackgroundPaint, mTextPaint;
        Typeface tf;

        int hours, minutes, seconds, weekday, Date;
        float battery;

        public drawingView(Context context, int hours, int minutes, int seconds, int weekday, int Date, float battery){
            super(context);

            tf = Typeface.createFromAsset(getActivity().getAssets(), "Inconsolata-Regular.ttf");

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(getActivity().getColor(R.color.colorPrimary));

            mTextPaint = new Paint();
            mTextPaint.setColor(getActivity().getColor(R.color.text));
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTypeface(tf);

            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            this.weekday = weekday;
            this.Date = Date;
            this.battery = battery;
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            float width = canvas.getWidth();
            float height = canvas.getHeight();

            canvas.drawRect(0,0,width,height,mBackgroundPaint);

            float centerX = width/2f;
            float centerY = height/2f;

            int cur_hour = hours;

            String cur_ampm = "AM";
            if(cur_hour == 0){
                cur_hour = 12;
            }
            if(cur_hour > 12){
                cur_hour = cur_hour - 12;
                cur_ampm = "PM";
            }

            String text = String.format("%02d:%02d:%02d", cur_hour,minutes,seconds);

            String days_of_week = "";
            switch(weekday){
                case 1: days_of_week = "MON";
                case 2: days_of_week = "TUE";
                case 3: days_of_week = "WED";
                case 4: days_of_week = "THU";
                case 5: days_of_week = "FRI";
                case 6: days_of_week = "SAT";
                case 7: days_of_week = "SUN";
            }
            String text2 = String.format("DATE: %s %d", days_of_week,Date);
            String batteryLevel = "BATTERY" + (int) battery + "%";

            canvas.drawText("00 00 00",centerX,centerY,mBackgroundPaint);

            mTextPaint.setColor(getActivity().getColor(R.color.text));
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
            canvas.drawText(text,centerX,centerY, mTextPaint);

            mTextPaint.setColor(getActivity().getColor(R.color.text));
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size_small));
            canvas.drawText(batteryLevel + " "+  text2,
                    centerX,
                    centerY + getResources().getDimension(R.dimen.text_size_small),
                    mTextPaint);
        }
    }
}
