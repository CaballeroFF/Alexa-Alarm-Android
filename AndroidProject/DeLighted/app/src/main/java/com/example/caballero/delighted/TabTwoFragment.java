package com.example.caballero.delighted;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabTwoFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "TabTwoFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_frag,container,false);

        return view;
    }
}
