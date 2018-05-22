package csusb.cse.alexadelighted.fragments;

import android.animation.Animator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import csusb.cse.alexadelighted.R;

public class ClockFragment extends Fragment {

    private static final String TAG = "DEBUG";

    private TextClock tClock,dClock;
    private TextView dayOfWeek;
    Button worldTime;

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        tClock = (TextClock) getActivity().findViewById(R.id.TextClock);
        dClock = (TextClock) getActivity().findViewById(R.id.TextDate);
        dayOfWeek = (TextView) getActivity().findViewById(R.id.TextDayOfWeek);

        View rootView = inflater.inflate(R.layout.clock_layout, container, false);
        return rootView;
    }
}
