package csusb.cse.alexadelighted.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import csusb.cse.alexadelighted.R;
import csusb.cse.alexadelighted.httprequests.PostURLContentTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";
    private ArrayList<String> mDataSet;
    private Context mContext;
    private String mURLString;
    private String mHttpPassString;

    private static View view;
    String responceCode = "null";

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mAlarm;
        ImageButton mTrash;
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView){
            super(itemView);
            Log.d(TAG, "ViewHolder: ");

            view = itemView;
            mAlarm = itemView.findViewById(R.id.alarm_recycler_text);
            mTrash = itemView.findViewById(R.id.recycler_trash);
            mRelativeLayout = itemView.findViewById(R.id.alarm_recycler_parent_layout);
        }
    }

    //constructor
    public RecyclerAdapter(Context mContext, ArrayList<String> mDataSet, String address) {
        Log.d(TAG, "RecyclerAdapter: ");
        this.mURLString = address;
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
                if(mURLString != null) {
                    mHttpPassString = mDataSet.get(position);
                    doPOST();
                    mDataSet.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDataSet.size());
                    if(!responceCode.equals("200")) {
                        Snackbar.make(view, "code: " + responceCode, Snackbar.LENGTH_SHORT).show();
                    }

//                  do we want to let the user erase if there is no connection??
//                    if(responceCode.equals("200")) {
//                        mDataSet.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position, mDataSet.size());
//                    } else {
//                        Snackbar.make(view, "code: " + responceCode, Snackbar.LENGTH_SHORT).show();
//                    }

                } else {
                    Snackbar.make(view, "URL is null", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return mDataSet.size();
    }

    public void doPOST(){
        Log.d(TAG, "doPOST: out string " + mHttpPassString);

        if(mURLString != null){
            PostURLContentTask postURLContentTask = new PostURLContentTask();
            postURLContentTask.execute(mURLString + "deletealarm" , mHttpPassString);
            try {
                responceCode = postURLContentTask.get();
                Log.d(TAG, "doPOST:......ra.......... " + postURLContentTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
//            Snackbar.make(view.findViewById(R.id.alarm_recycler_parent_layout),
//                    "code: " + responceCode, Snackbar.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "doPOST: URL is null");
        }
    }
}