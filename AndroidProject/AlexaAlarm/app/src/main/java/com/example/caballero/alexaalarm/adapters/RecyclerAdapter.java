package com.example.caballero.alexaalarm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caballero.alexaalarm.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";
    private ArrayList<String> mDataSet;
    private Context mContext;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mAlarm;
        ImageButton mTrash;
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView){
            super(itemView);
            Log.d(TAG, "ViewHolder: ");

            mAlarm = itemView.findViewById(R.id.alarm_recycler_text);
            mTrash = itemView.findViewById(R.id.recycler_trash);
            mRelativeLayout = itemView.findViewById(R.id.alarm_recycler_parent_layout);
        }
    }

    //constructor
    public RecyclerAdapter(Context mContext, ArrayList<String> mDataSet) {
        Log.d(TAG, "RecyclerAdapter: ");
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_recycler,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        holder.mAlarm.setText(mDataSet.get(position));
        holder.mTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataSet.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return mDataSet.size();
    }
}
