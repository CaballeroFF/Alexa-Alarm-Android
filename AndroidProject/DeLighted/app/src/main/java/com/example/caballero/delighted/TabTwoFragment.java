package com.example.caballero.delighted;

import android.animation.Animator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

import java.net.URL;
import java.util.Timer;

public class TabTwoFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "TabTwoFragment";

    private TextClock tClock,dClock;
    private TextView dayOfWeek;
    private Button worldTime;

    String url = "https://www.worldtimeserver.com/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_frag,container,false);

        tClock = (TextClock) getActivity().findViewById(R.id.TextClock);
        dClock = (TextClock) getActivity().findViewById(R.id.TextDate);
        dayOfWeek = (TextView) getActivity().findViewById(R.id.TextDayOfWeek);
        Button worldTime = view.findViewById(R.id.worldTimeBttn);

        worldTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl();
            }
        });

        return view;
    }

    public void goToSu (View view) {
        goToUrl ();
    }

    private void goToUrl () {
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(url));
        startActivity(viewIntent);
    }
}
