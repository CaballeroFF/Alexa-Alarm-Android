package com.example.caballero.alexaalarm.httprequests;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostURLContentTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "DEBUG";

    @Override
    protected String doInBackground(String... args) {
        String post = "";

        try {
            post = args[1];
            URL url = new URL(args[0]);
            Log.d(TAG, "doInBackground: " + url.toString());
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.setRequestMethod("POST");
            request.addRequestProperty("Content-Length", Integer.toString(post.length()));
            request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            request.setDoOutput(true);
            request.connect();

            OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream());
            writer.write(post);
            writer.flush();
            writer.close();

            int responceCode = request.getResponseCode();
            Log.d(TAG, "doInBackground: response code" + Integer.toString(responceCode));
            request.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress){
        Log.d(TAG, "onProgressUpdate: ");
    }

    protected void onPostExecute(String result){
        Log.d(TAG, "onPostExecute: " + result);
    }

}
