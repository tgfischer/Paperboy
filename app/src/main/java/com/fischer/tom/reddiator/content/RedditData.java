package com.fischer.tom.reddiator.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * Credit to http://www.whycouch.com/2012/12/how-to-create-android-client-for-reddit.html
 */
public class RedditData {
    public static HttpURLConnection getConnection(String url) {
        HttpURLConnection connection = null;

        try {
            URL connectionURL = new URL(url);

            connection = (HttpURLConnection)connectionURL.openConnection();
            connection.setReadTimeout(30000);                               // Timeout at 30 seconds
            connection.setRequestProperty("User-Agent", "Alien V1.0");
        } catch (MalformedURLException e) {
            Log.e("getConnection()", "Invalid URL: " + e.toString());
        } catch (IOException e) {
            Log.e("getConnection()", "Could not connect: " + e.toString());
        }

        return connection;
    }

    public static String getJSON(String url){
        HttpURLConnection connection = getConnection(url);

        if (connection == null) {
            return null;
        }

        String json = null;

        try{
            json = "";
            String line = "";

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((line = br.readLine()) != null) {
                json += line + "\n";
            }

            br.close();
        } catch(IOException e) {
            Log.d("READ FAILED", e.getMessage());
        } finally {
            connection.disconnect();
        }

        return json;
    }
}
