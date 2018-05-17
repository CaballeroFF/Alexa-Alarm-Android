package com.example.caballero.alexaalarm.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.caballero.alexaalarm.MainActivity;
import com.example.caballero.alexaalarm.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";

    EditText mURLText;
    Button mSetURL;

    private final String PROTOCOL = "https://";
    private final String DOMAIN = ".ngrok.io/";
    private String mURLString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d(TAG, "onCreate: SettingsActivity()");

        mURLText = findViewById(R.id.address_settings_text);
        mSetURL = findViewById(R.id.set_address);

        mSetURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mURLString = PROTOCOL + mURLText.getText().toString() + DOMAIN;
                Snackbar.make(view, "url set to " + mURLString, Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: SettingsActivity()");
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("toMain", mURLString);
        startActivity(intent);
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
