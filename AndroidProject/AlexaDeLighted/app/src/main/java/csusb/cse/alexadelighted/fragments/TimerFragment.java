package csusb.cse.alexadelighted.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import csusb.cse.alexadelighted.R;

public class TimerFragment extends Fragment {

    private static final String TAG = "DEBUG";

    GridLayout numberSelectGrid;
    Button dayButton, hourButton, minuteButton, setTimer;
    StringBuilder mString = new StringBuilder();
    String currentTimerString;

    DataPassListener mCallback;

    public interface DataPassListener{
        void passDuration(String string);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (DataPassListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement passDuration()");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.timer_layout, container, false);
        numberSelectGrid = rootView.findViewById(R.id.timer_number_grid);
        dayButton = rootView.findViewById(R.id.day_timer);
        hourButton = rootView.findViewById(R.id.hour_timer);
        minuteButton = rootView.findViewById(R.id.minute_timer);
        setTimer = rootView.findViewById(R.id.set_grid_timer);

        int childCountPad = numberSelectGrid.getChildCount();

        gridListener(numberSelectGrid, childCountPad);

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTimerString = "day";
                dayButton.setTextColor(Color.parseColor("#4286f4"));
                hourButton.setTextColor(Color.parseColor("#000000"));
                minuteButton.setTextColor(Color.parseColor("#000000"));
                mString.setLength(0);
            }
        });

        hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTimerString = "hour";
                dayButton.setTextColor(Color.parseColor("#000000"));
                hourButton.setTextColor(Color.parseColor("#4286f4"));
                minuteButton.setTextColor(Color.parseColor("#000000"));
                mString.setLength(0);
            }
        });

        minuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTimerString = "min";
                dayButton.setTextColor(Color.parseColor("#000000"));
                hourButton.setTextColor(Color.parseColor("#000000"));
                minuteButton.setTextColor(Color.parseColor("#4286f4"));
                mString.setLength(0);
            }
        });

        setTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(dayButton.getText().toString()).append(" ");
                stringBuilder.append(hourButton.getText().toString()).append(" ");
                stringBuilder.append(minuteButton.getText().toString());
                if(!stringBuilder.toString().equals("00 00 00")) {
                    Log.d(TAG, "onClick: setTimer" + stringBuilder.toString());
                    mCallback.passDuration(stringBuilder.toString());
                }
            }
        });

        return rootView;
    }

    //region Description: UI Layout code
    public void gridListener(GridLayout gridLayout, int childCount){
        for(int i= 0; i < childCount; i++){
            if(i == 9){
                i = 10;
            }
            final Button button = (Button) gridLayout.getChildAt(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: " + button.getText().toString());
                    getString(button.getText().toString());
                    setString(currentTimerString);
                }
            });
        }
    }

    public void getString(String s){
        if(s.equals("Del")){
            if(mString.length() > 0) {
                mString.setLength(mString.length() - 1);
            }
        } else {
            //only allow 2 digits
            if(mString.length() <= 1 && !s.equals("Set")){
                mString.append(s);
                //delete leading zero
                if(mString.length() > 0){
                    if(Character.toString(mString.charAt(0)).equals("0")){
                        mString = mString.deleteCharAt(0);
                    }
                }
            }
        }
    }

    public void setString(String s){

        if(s == null){
            s = "";
        }

        NumberFormat format = new DecimalFormat("00");

        if(mString.toString().equals("")){
            Log.d(TAG, "setString: ");
            mString.append("0");
        }

        int number = Integer.parseInt(mString.toString());

        switch (s){
            case "day":
                dayButton.setText(format.format(number));
                break;
            case "hour":
                hourButton.setText(format.format(number));
                break;
            case "min":
                minuteButton.setText(format.format(number));
                break;
            default:
                break;
        }
    }
    //endregion
}