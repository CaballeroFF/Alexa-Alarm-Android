package com.example.caballero.delighted;

import java.util.Locale;

public class DateFormater {

    public DateFormater(){

    }

    public String formatedDate(int year, int month, int day, int hour, int minute){

        String fMonth = String.format(Locale.US,"%02d", month);
        String fDay = String.format(Locale.US,"%02d", day);
        String fHour = String.format(Locale.US,"%02d", hour);
        String fMinute = String.format(Locale.US,"%02d", minute);

        String s = Integer.toString(year) + "-" + fMonth + "-" +
                fDay + "  " + fHour + ":" + fMinute;
        return s;
    }

    public String formatedDuration(String day, String hour, String minute){
        //string bools
        String d = "1";
        String h = "1";
        String m = "1";
        if(day.equals("0")){
            d = "0";
        }
        if (hour.equals("0")){
            h = "0";
        }
        if(minute.equals("0")){
            m = "0";
        }
        //set cases
        String cases = d + h + m;
        String duration = "";
        switch (cases){
            //no duration
            case "000":
                duration = "P0D";
                break;
            //only minutes
            case "001":
                duration = String.format("PT%sM", minute);
                break;
            //only hours
            case "010":
                duration = String.format("PT%sH", hour);
                break;
            //hours and munutes
            case "011":
                duration = String.format("PT%sH%sM", hour, minute);
                break;
            //only days
            case "100":
                duration = String.format("P%sD", day);
                break;
            //days and minutes
            case "101":
                duration = String.format("P%sDT%sM", day, minute);
                break;
            //days and hours
            case "110":
                duration = String.format("P%sDT%sH", day, hour);
                break;
            //complete duration
            default:
                duration = String.format("P%sDT%sH%sM", day, hour, minute);
                break;
        }
        return duration;
    }

}

