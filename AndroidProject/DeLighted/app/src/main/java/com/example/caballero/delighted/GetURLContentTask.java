package com.example.caballero.delighted;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class GetURLContentTask extends AsyncTask<String, Integer, String>{

    @Override
    protected String doInBackground(String... urls){
        StringBuilder result = new StringBuilder();
        try{
            URL url = new URL(urls[0]);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            request.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null){
                result.append(line);
            }

            request.disconnect();

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result.toString();
    }

    protected void onProgressUpdate(Integer... progress){
    }

    protected void onPostExecute(String result){
        Log.v("DEBUG", "GET..." + result);
    }
}

