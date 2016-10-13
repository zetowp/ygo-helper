package com.jzoft.ygohelper.biz.impl;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jjimenez on 11/10/16.
 */
public class GetCall extends AsyncTask<String, Void, InputStream> {

    @Override
    protected InputStream doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            return urlConnection.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
}
