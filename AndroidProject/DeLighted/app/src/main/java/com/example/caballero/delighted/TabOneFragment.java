package com.example.caballero.delighted;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class TabOneFragment extends android.support.v4.app.Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        NumberPicker.OnValueChangeListener{

    private EditText outPutText;
    private EditText URLText;

    private final String PROTOCOL = "https://";
    private final String DOMAIN = ".ngrok.io/";

    private String URLString;
    private String URLFileName;
    private String URLPath;
    private String outPutString;
    private int year, month, day, hour, minute;

    private String TAG = "DEBUG";

//    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_frag,container,false);

        outPutText = view.findViewById(R.id.OutPutText);
        URLText = view.findViewById(R.id.URLText);
        Button setURLButton = view.findViewById(R.id.SetURLButton);
        Button setAlarmDateButton = view.findViewById(R.id.pick_date);
        Button setAlarmDurationButton = view.findViewById(R.id.pick_duration);
        Button listAlamrsButton = view.findViewById(R.id.list_alarms);
        Button clearAlarmsButton = view.findViewById(R.id.clear_alarms);
        Button deleteAlarmsButton = view.findViewById(R.id.delete_alarm);
        Button snoozeAlarmButton = view.findViewById(R.id.snooze_alarm);
        Button stopAlarmButton = view.findViewById(R.id.stop_alarm);

        setURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLString = PROTOCOL + URLText.getText().toString() + DOMAIN;
                Log.v(TAG, "URL: " + URLString);
            }
        });

        //set URLPath on every Button to correct path
        setAlarmDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "setalarm";
                getCalendarInstance();
                pickTime();
            }
        });

        setAlarmDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "setalarm";
                showDurationDialog();
            }
        });

        listAlamrsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "listalarms";
                doGet();
                //clear outputstring every time after use
                outPutString = null;
            }
        });

        clearAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "clearalarms";
                doGet();
                //clear outputstring every time after use
                outPutString = null;
            }
        });

        deleteAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "deletealarm";
                pickTime();
            }
        });

        snoozeAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if alarm is active
                URLPath = "listalarms";
                doGet();

                getCalendarInstance();
                String s = new DateFormater().formatedDate(year, month, day, hour, minute);
                String[] sl = s.split(" ");

                String outMessage = outPutString + "...." + sl[0];
                boolean areEqual = sl[0].equals(outPutString);

                if(areEqual){
                    outMessage = "stopping...";
                    URLPath = "deletealarm";
                    outPutString = s;
                    doPost();

                    //wait for doPost to run
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            URLPath = "setalarm";
                            String s = new DateFormater().formatedDate(year, month, day, hour, minute + 5);
                            outPutString = s;
                            doPost();
                        }
                    }, 250);
                }
                //clear string after use
                outPutString = null;
                outPutText.setText(outMessage);
            }
        });

        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if alarm is active
                URLPath = "listalarms";
                doGet();

                getCalendarInstance();
                String s = new DateFormater().formatedDate(year, month, day, hour, minute);
                String[] sl = s.split(" ");

                String outMessage = outPutString + "...." + sl[0];
                boolean areEqual = sl[0].equals(outPutString);

                if(areEqual){
                    outMessage = "stopping...";
                    URLPath = "deletealarm";
                    outPutString = s;
                    doPost();
                }
                //clear string after use
                outPutString = null;
                outPutText.setText(outMessage);
            }
        });
		
		return view;
    }

    //HTTP requests
    public void doPost(){
        if(URLString != null){
            PostURLContentTask postURLContentTask = new PostURLContentTask();
            postURLContentTask.execute(URLString + URLPath, outPutString);
        } else {
            Log.v(TAG, "URLString is null");
        }
    }

    public void doGet(){
        if(URLString != null){
            GetURLContentTask getURLContentTask = new GetURLContentTask();
            getURLContentTask.execute(URLString + URLPath);
            try{
                outPutString = getURLContentTask.get();
                outPutString = outPutString.replace("\"", "");
            } catch (InterruptedException e){
                e.printStackTrace();
            } catch (ExecutionException e){
                e.printStackTrace();
            }

            Log.v(TAG, outPutString);
            outPutText.setText(outPutString);
        } else {
            Log.v(TAG, "URLString is null");
        }
    }

    // repeated code for calendar made into a function instead
    public void getCalendarInstance(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    // dialog code
    public void pickTime(){
        //call getCalendarInstance() before this function
        //open dialog to pick date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), TabOneFragment.this,
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        // runs after "ok" is pressed on dialog
        year = i;
        month = i1 + 1;
        day = i2;

        //run time dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), TabOneFragment.this,
                hour, minute, DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        // runs after "ok" is pressed
        hour = i;
        minute = i1;
        //format into hh:mm yyyy-mm-dd
        String s = new DateFormater().formatedDate(year, month, day, hour, minute);

        outPutString = s;
        outPutText.setText(s);
        //run HTTP POST
        doPost();
        outPutString = null;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Log.i(TAG,"..."+i1);
    }

    public void showDurationDialog(){
        //setup dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("pick");
        dialog.setContentView(R.layout.dialog);

        Button select = dialog.findViewById(R.id.setButton);
        Button cancel = dialog.findViewById(R.id.cancelButton);
        final NumberPicker numberDayPicker = dialog.findViewById(R.id.numberPickerDay);
        final NumberPicker numberHourPicker = dialog.findViewById(R.id.numberPickerHour);
        final NumberPicker numberMinutePicker = dialog.findViewById(R.id.numberPickerMinute);

        //setup day number picker
        numberDayPicker.setMaxValue(365);
        numberDayPicker.setMinValue(0);
        numberDayPicker.setWrapSelectorWheel(false);
        numberDayPicker.setOnValueChangedListener(this);
        //setup hour number picker
        numberHourPicker.setMaxValue(24);
        numberHourPicker.setMinValue(0);
        numberHourPicker.setWrapSelectorWheel(false);
        numberHourPicker.setOnValueChangedListener(this);
        //setup minute number picker
        numberMinutePicker.setMaxValue(60);
        numberMinutePicker.setMinValue(0);
        numberMinutePicker.setWrapSelectorWheel(false);
        numberMinutePicker.setOnValueChangedListener(this);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day = String.valueOf(numberDayPicker.getValue());
                String hour = String.valueOf(numberHourPicker.getValue());
                String minute = String.valueOf(numberMinutePicker.getValue());
                String duration = new DateFormater().formatedDuration(day, hour, minute);
                outPutString = duration;
                outPutText.setText(duration);

                //run HTTP POST
                doPost();
                dialog.dismiss();
                outPutString = null;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
