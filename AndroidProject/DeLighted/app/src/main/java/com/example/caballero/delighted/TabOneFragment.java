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

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class TabOneFragment extends android.support.v4.app.Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        NumberPicker.OnValueChangeListener{

    private EditText outPutText;

    private EditText URLText;
    private Button getButton;
    private Button postButton;
    private Button setURLButton;
    private Button pick;
    private Button test;

    private String URLString;
    private String outPutString;
    private int year, month, day, hour, minute;

    private static final String TAG = "DEBUG";

//    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_frag,container,false);
        outPutText = (EditText) view.findViewById(R.id.OutPutText);
        setURLButton = (Button) view.findViewById(R.id.SetURLButton);
        URLText = (EditText) view.findViewById(R.id.URLText);
        getButton = (Button)view.findViewById(R.id.GETRequest);
        postButton = (Button) view.findViewById(R.id.POSTRequest);
        pick = (Button) view.findViewById(R.id.pick_date);
        test = (Button) view.findViewById(R.id.test);

 //       return view;

  //  }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getActivity().setContentView(R.layout.tab1_frag);

//        outPutText =  getActivity().findViewById(R.id.OutPutText);
 //       URLText = getActivity().findViewById(R.id.URLText);
 //       getButton = getActivity().findViewById(R.id.GETRequest);
//        postButton = getActivity().findViewById(R.id.POSTRequest);
//        setURLButton = getView().findViewById(R.id.SetURLButton);
 //       pick = getActivity().findViewById(R.id.pick_date);
 //       test = getActivity().findViewById(R.id.test);

        Log.v("TEST", "TEST");

        setURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URLString = URLText.getText().toString();
                Log.v(TAG, "URL: " + URLString);
            }
        });

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(URLString != null){
                    GetURLContentTask getURLContentTask = new GetURLContentTask();
                    getURLContentTask.execute(URLString);
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
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outPutString = outPutText.getText().toString();
                if(URLString != null){
                    PostURLContentTask postURLContentTask = new PostURLContentTask();
                    postURLContentTask.execute(URLString, outPutString);
                } else {
                    Log.v(TAG, "URLString is null");
                }
            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), TabOneFragment.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year = i;
        month = i1 + 1;
        day = i2;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), TabOneFragment.this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour = i;
        minute = i1;

        String s = new DateFormater().formatedDate(year, month, day, hour, minute);
        outPutText.setText(s);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Log.i(TAG,"..."+i1);
    }

    public void show(){
        //setup dialog
        final Dialog dialog = new Dialog(getActivity());
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
