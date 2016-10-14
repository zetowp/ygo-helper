package com.jzoft.ygohelper.utils.impl;

import com.jzoft.ygohelper.utils.HttpCaller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by jjimenez on 13/10/16.
 */
public class HttpCallerConnection implements HttpCaller {

    @Override
    public byte[] getCall(String url) throws NotFound {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            evaluateResponse(urlConnection, url);
            return getBytes(urlConnection.getInputStream());
        } catch (UnknownHostException e) {
            throw new NotFound(url);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void evaluateResponse(HttpURLConnection urlConnection, String location) throws IOException, NotFound {
        if (urlConnection.getResponseCode() == 404) {
            throw new NotFound(location);
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        inputStream.close();
        return byteBuffer.toByteArray();
    }
}
