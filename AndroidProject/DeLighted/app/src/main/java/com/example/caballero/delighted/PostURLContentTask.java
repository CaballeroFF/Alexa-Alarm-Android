package com.example.caballero.delighted;

import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostURLContentTask extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... args){
        String post = "";
        try{
            URL url = new URL(args[0]);
            Log.v("DEBUG", args[0]);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            post = args[1];

            request.setRequestMethod("POST");
            request.addRequestProperty("Content-Length", Integer.toString(post.length()));
            request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            request.setDoOutput(true);
            request.connect();

            OutputStreamWriter writer = new OutputStreamWriter(request.getOutputStream());
            writer.write(post);
            writer.flush();
            writer.close();

            int code = request.getResponseCode();
            Log.v("DEBUG", "CODE: " + Integer.toString(code));
            request.disconnect();
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return post;
    }

    protected void onProgressUpdate(Integer... progress){
    }

    protected void onPostExecute(String result){
        Log.v("DEBUG", "POST..." + result);
    }
}
