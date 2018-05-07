package com.example.caballero.delighted;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import android.text.format.DateFormat;

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

    private String TAG = "HTTPREQUEST";

//    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_frag,container,false);

        outPutText = findViewById(R.id.OutPutText);
        URLText = findViewById(R.id.URLText);
        Button setURLButton = findViewById(R.id.SetURLButton);
        Button setAlarmDateButton = findViewById(R.id.pick_date);
        Button setAlarmDurationButton = findViewById(R.id.pick_duration);
        Button listAlamrsButton = findViewById(R.id.list_alarms);
        Button clearAlarmsButton = findViewById(R.id.clear_alarms);
        Button deleteAlarmsButton = findViewById(R.id.delete_alarm);

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
            }
        });

        clearAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "clearalarms";
                doGet();
            }
        });

        deleteAlarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLPath = "deletealarm";
                pickTime();
            }
        });
		
		return view;
    }

    //HTTP requests
    public void doPost(){
        outPutString = outPutText.getText().toString();
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

    // dialog code
    public void pickTime(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //open dialog to pick date
        DatePickerDialog datePickerDialog = new DatePickerDialog(TabOneFragment.this, TabOneFragment.this,
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        // runs after "ok" is pressed on dialog
        year = i;
        month = i1 + 1;
        day = i2;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        //run time dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(TabOneFragment.this, TabOneFragment.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        // runs after "ok" is pressed
        hour = i;
        minute = i1;
        //format into hh:mm yyyy-mm-dd
        String s = new DateFormater().formatedDate(year, month, day, hour, minute);

        outPutText.setText(s);
        //run HTTP POST
        doPost();
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Log.i(TAG,"..."+i1);
    }

    public void showDurationDialog(){
        //setup dialog
        final Dialog dialog = new Dialog(TabOneFragment.this);
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
                outPutText.setText(duration);

                //set rout/path to setalarm
                URLPath = "setalarm";
                //run HTTP POST
                doPost();
                dialog.dismiss();
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
