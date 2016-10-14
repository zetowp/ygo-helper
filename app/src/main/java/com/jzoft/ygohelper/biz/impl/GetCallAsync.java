package com.jzoft.ygohelper.biz.impl;

import android.os.AsyncTask;

import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.impl.HttpCallerConnection;

import java.util.concurrent.ExecutionException;

/**
 * Created by jjimenez on 11/10/16.
 */
public class GetCallAsync extends AsyncTask<String, Void, byte[]> implements HttpCaller {

    private final HttpCallerConnection caller;
    private NotFound notFound;

    public GetCallAsync() {
        caller = new HttpCallerConnection();
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        try {
            return caller.getCall(strings[0]);
        } catch (NotFound e) {
            notFound = e;
            return null;
        }
    }

    @Override
    public byte[] getCall(String url) throws NotFound {
        try {
            byte[] bytes = execute(url).get();
            if (notFound != null)
                throw notFound;
            return bytes;
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }
}
