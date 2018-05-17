package com.example.caballero.alexaalarm.httprequests;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetURLContentTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "DEBUG";

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder result = new StringBuilder();
        Log.d(TAG, "doInBackground: GET");

        try{
            URL url = new URL(urls[0]);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            request.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;

            while((line = reader.readLine()) != null){
                result.append(line);
            }

            request.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result.toString();
    }

    protected void onProgressUpdate(Integer... progress){
        Log.d(TAG, "onProgressUpdate: ");
    }

    protected void onPostExecute(String result){
        Log.d(TAG, "onPostExecute: " + result);
    }
}
