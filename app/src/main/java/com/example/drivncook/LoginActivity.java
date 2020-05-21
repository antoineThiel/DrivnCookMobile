package com.example.drivncook;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AsyncTask<String, Void, String> {

    private Context context;
    StringBuilder sb=null;
    BufferedReader reader = null;
    String serverResponse = null;

    public LoginActivity(Context context)
    {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        try{
            String username = args[0];
            String password = args[1];
            String link = "http://51.210.7.226/customer/getlog/"+username+"/"+password;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int statusCode = connection.getResponseCode();
            if (statusCode == 200)
            {
                sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = reader.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
            }
            connection.disconnect();
            if (sb != null)
            {
                serverResponse = sb.toString();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (reader != null)
            {
                try {
                    reader.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return serverResponse;
    }
}
