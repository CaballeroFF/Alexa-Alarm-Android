package csusb.cse.alexadelighted.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import csusb.cse.alexadelighted.R;

public class StopWatchFragment extends Fragment {

    private static final String TAG = "DEBUG";

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        View rootView = inflater.inflate(R.layout.stop_watch_layout, container, false);
        textView = rootView.findViewById(R.id.stop_watch_text);
        return rootView;
    }
}
