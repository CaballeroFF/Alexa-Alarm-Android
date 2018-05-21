package csusb.cse.alexadelighted;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import csusb.cse.alexadelighted.activities.SettingsActivity;
import csusb.cse.alexadelighted.adapters.SectionsPagerAdapter;
import csusb.cse.alexadelighted.fragments.AlarmFragment;
import csusb.cse.alexadelighted.fragments.ClockFragment;
import csusb.cse.alexadelighted.fragments.StopWatchFragment;
import csusb.cse.alexadelighted.fragments.TimerFragment;
import csusb.cse.alexadelighted.helpers.DateFormater;
import csusb.cse.alexadelighted.httprequests.GetURLContentTask;
import csusb.cse.alexadelighted.httprequests.PostURLContentTask;

public class MainActivity extends AppCompatActivity implements TimerFragment.DataPassListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "DEBUG";

    private ViewPager mViewPager;

    FloatingActionButton fab;

    StringBuilder mDuration = new StringBuilder();
    private ArrayList<String> mDataSet = new ArrayList<>();

    // region Description: Http values/variables
    //example 'https://github.com'
    private String mURLString;
    //example https://github.com'/pulls'
    private String mURLPath;
    //string used for GET and POST
    private String mHttpPassString;
    String responseCode = "null";
    //endregion

    // region Description: date picker values/variables
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute = 1;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);

        //region Description: restore saved data
        SharedPreferences sharedPreferences = getSharedPreferences("MAIN", Context.MODE_PRIVATE);
        if(getIntent().getStringExtra("toMain") != null) {
            Log.d(TAG, "onCreate: Intent " + getIntent().getStringExtra("toMain"));
            mURLString = getIntent().getStringExtra("toMain");
        } else {
            mURLString = sharedPreferences.getString("mURLString", null);
        }
        String s = sharedPreferences.getString("mDataSet", null);
        mDataSet = stringToArray(sharedPreferences.getString("mDataSet", null));
        Log.d(TAG, "onCreate: MainActivity()" + mURLString + mDataSet);
        //endregion

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        final TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //need this to be able to click on first run
        if(tabLayout.getSelectedTabPosition() == 0){
            alarmAddButtonListener();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //note Snackbars are only used for debugging

                int currentPosition = tabLayout.getSelectedTabPosition();
                Log.d(TAG, "onTabSelected: " + Integer.toString(currentPosition));

                switch (currentPosition){
                    case 0:
                        Log.d(TAG, "onTabSelected: data " + mDataSet);
                        fab.setEnabled(true);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(android.R.drawable.ic_input_add);
                        alarmAddButtonListener();
                        //todo: look into another way to save data in fragment instead
                        updateArray();
                        sendAlarmData();
                        break;
                    case 1:
                        fab.setEnabled(true);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.world);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "WORLD", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 2:
                        fab.setEnabled(false);
                        fab.setVisibility(View.GONE);
                        break;
                    case 3:
                        fab.setEnabled(true);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(android.R.drawable.ic_media_play);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "STOP WATCH", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    default:
                        fab.setEnabled(true);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(android.R.drawable.ic_input_add);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "non existent tab", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected: ");
            }
        });

    }

    //region Description: save data
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("MAIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mURLString", mURLString);
        editor.putString("mDataSet", arrayToString(mDataSet));
        editor.apply();
        Log.d(TAG, "onPause: " + mURLString + " " + mDataSet);
    }

    public String arrayToString(ArrayList<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        if(!list.isEmpty()) {
            for (String s : list) {
                stringBuilder.append(s).append(">");
            }
            return stringBuilder.toString();
        }
        return null;
    }

    public ArrayList<String> stringToArray(String s){
        ArrayList<String> returnArray = new ArrayList<>();
        if(s != null) {
            returnArray = new ArrayList<>(Arrays.asList(s.split(">")));
        }
        return returnArray;
    }
    //endregion

    //region Description: menu override methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_settings){
            Snackbar.make(this.findViewById(R.id.main_content), "settings",
                    Snackbar.LENGTH_SHORT)
                    .show();
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    public void setupViewPager(ViewPager viewPager){
        Log.d(TAG, "setupViewPager: ");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AlarmFragment(), "alarm");
        adapter.addFragment(new ClockFragment(), "clock");
        adapter.addFragment(new TimerFragment(), "timer");
        adapter.addFragment(new StopWatchFragment(), "stop watch");
        viewPager.setAdapter(adapter);
    }

    //region Description: interface methods, send/receive from fragments
    //Timer Fragment interface method
    @Override
    public void passDuration(String string) {
        //method to pass data from TimerFragment to main activity
        if(mURLString != null) {
            mURLPath = "setalarm";
            mHttpPassString = new DateFormater().addDuration(string);
            Log.d(TAG, "passDuration: raw=" + string + " formated=" + mHttpPassString);
            //todo: uncomment doPost
            doPOST();
            //successful request code 200
            if(responseCode.equals("200")){
                mDataSet.add(mHttpPassString);
            } else {
                Snackbar.make(this.findViewById(R.id.main_content), "code: " + responseCode, Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
            Snackbar.make(mViewPager, "URL is null", Snackbar.LENGTH_SHORT).show();
        }

    }

    public void sendAlarmData(){
        AlarmFragment alarmFragment = new AlarmFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("ALARM", mDataSet);
        bundle.putString("ADDRESS", mURLString);
        alarmFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.alarm_relative_layout, alarmFragment)
                .commit();
    }
    //endregion

    //region Description: date picker code
    public void getCalendarInstance(){
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        Log.d(TAG, "getCalendarInstance: minute " + mMinute);
    }

    public void pickTime(){
        getCalendarInstance();

        Log.d(TAG, "pickTime: minute " + mMinute);
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, MainActivity.this,
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mYear = i;
        mMonth = i1 + 1;
        mDay = i2;
        Log.d(TAG, "onDateSet: minute " + mMinute);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, MainActivity.this,
                mHour, mMinute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        mHour = i;
        mMinute = i1;
        Log.d(TAG, "onTimeSet: minute " + mMinute);

        mHttpPassString = new DateFormater().formatedDate(mYear, mMonth, mDay, mHour, mMinute);
        Log.d(TAG, "onTimeSet: " + mHttpPassString);
        //todo: uncomment doPost
        doPOST();
        if(responseCode.equals("200")){
            mDataSet.add(mHttpPassString);
        } else {
            Snackbar.make(this.findViewById(R.id.main_content), "code: " + responseCode, Snackbar.LENGTH_SHORT)
                    .show();
        }
        sendAlarmData();
    }

    public void alarmAddButtonListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mURLString != null) {
                    mURLPath = "setalarm";
                    pickTime();
                } else {
                    Snackbar.make(view, "URL is null", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mURLPath = "listalarms";
                doGet();
                String trimmedString = mHttpPassString.replaceAll("\"", "");
                Snackbar.make(view, trimmedString, Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "onLongClick: " + trimmedString);

                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(trimmedString.split(",")));
                Log.d(TAG, "onLongClick: " + arrayList);
                return false;
            }
        });
    }
    //endregion

    //region Description: HTTP Requests
    public void doGet(){
        //todo: can it be made a parameter
        //todo: get response code

        if(mURLString != null){
            GetURLContentTask getURLContentTask = new GetURLContentTask();
            getURLContentTask.execute(mURLString + mURLPath);
            try {
                mHttpPassString = getURLContentTask.get();
                Log.d(TAG, "doGet: " + mHttpPassString);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "doGet: URL is null");
        }
    }

    public void doPOST(){
        //todo: make it return response code string
        //todo: can it take parameters
        Log.d(TAG, "doPOST: out string " + mHttpPassString);

        if(mURLString != null){
            PostURLContentTask postURLContentTask = new PostURLContentTask();
            postURLContentTask.execute(mURLString + mURLPath, mHttpPassString);
            try {
                responseCode = postURLContentTask.get();
                Log.d(TAG, "doPOST:................ " + postURLContentTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "doPOST: URL is null");
        }
    }
    //endregion

    //region Description: update data array
    public void updateArray(){
        //todo: this makes tabs slow, think of a policy to refresh
        mURLPath = "listalarms";
        doGet();
        String trimmedString = mHttpPassString.replaceAll("\"", "");
        Log.d(TAG, "updateArray: " + trimmedString + Boolean.toString(trimmedString.equals("")));
        if(!trimmedString.equals("")) {
            mDataSet = new ArrayList<>(Arrays.asList(trimmedString.split(",")));
        } else {
            mDataSet = new ArrayList<>();
        }
    }
    //endregion
}