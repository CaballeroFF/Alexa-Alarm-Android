package csusb.cse.alexadelighted.helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormater {

    private static final String TAG = "DEBUG";

    public DateFormater() {
    }

    public String addDuration(String time){
        String[] timeArray = time.split(" ");
        int day = Integer.parseInt(timeArray[0]);
        int hour = Integer.parseInt(timeArray[1]);
        int minute = Integer.parseInt(timeArray[2]);

        int totalMinutes = minute + (60 * hour) + (1440 * day);

        Calendar calendar = Calendar.getInstance();
        String date = formatedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm yyyy-MM-dd",
                Locale.US);

        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.add(Calendar.MINUTE, totalMinutes);

        date = simpleDateFormat.format(calendar.getTime());

        return date;
    }

    public String formatedDate(int year, int month, int day, int hour, int minute){

        String fMonth = String.format(Locale.US,"%02d", month);
        String fDay = String.format(Locale.US,"%02d", day);
        String fHour = String.format(Locale.US,"%02d", hour);
        String fMinute = String.format(Locale.US,"%02d", minute);

        return fHour + ":" + fMinute + " " + Integer.toString(year) + "-" + fMonth + "-" + fDay;
    }
}
