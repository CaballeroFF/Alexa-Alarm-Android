package com.example.caballero.alexaalarm.helpers;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Locale;

public class DateFormater {

    private static final String TAG = "DEBUG";

    public DateFormater() {
    }

    public String formatedDate(int year, int month, int day, int hour, int minute){

        String fMonth = String.format(Locale.US,"%02d", month);
        String fDay = String.format(Locale.US,"%02d", day);
        String fHour = String.format(Locale.US,"%02d", hour);
        String fMinute = String.format(Locale.US,"%02d", minute);

        return fHour + ":" + fMinute + " " + Integer.toString(year) + "-" + fMonth + "-" + fDay;
    }

    @SuppressLint("DefaultLocale")
    public String formatedDUration(String time){
        String[] timeArray = time.split(" ");
        int day = Integer.parseInt(timeArray[0]);
        int hour = Integer.parseInt(timeArray[1]);
        int minute = Integer.parseInt(timeArray[2]);

        //string bools
        String d = "1";
        String h = "1";
        String m = "1";
        if(day == 0){
            d = "0";
        }
        if(hour == 0){
            h = "0";
        }
        if(minute == 0){
            m = "0";
        }

        //set cases
        String cases = d + h + m;
        String duration;
        switch (cases){
            //no duration
            case "000":
                duration = "P0D";
                break;
            //only minutes
            case "001":
                duration = String.format(Locale.US,"PT%dM", minute);
                break;
            //only hours
            case "010":
                duration = String.format(Locale.US,"PT%dH", hour);
                break;
            //hours and munutes
            case "011":
                duration = String.format(Locale.US,"PT%dH%dM", hour, minute);
                break;
            //only days
            case "100":
                duration = String.format(Locale.US,"P%dD", day);
                break;
            //days and minutes
            case "101":
                duration = String.format(Locale.US,"P%dDT%dM", day, minute);
                break;
            //days and hours
            case "110":
                duration = String.format(Locale.US,"P%dDT%dH", day, hour);
                break;
            //complete duration
            default:
                duration = String.format(Locale.US,"P%dDT%dH%dM", day, hour, minute);
                break;
        }
        duration += " today";
        Log.d(TAG, "formatedDUration: " + duration);
        return duration;
    }
}
