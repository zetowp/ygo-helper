package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocalizator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchLocalizatorWikia implements PatchLocalizator {
    @Override
    public Patch localize(String location) {
        try {
            InputStream page = getResourceAtLocation(location);
            String urlImage = getImageUrlFromPage(page);
            InputStream image = getResourceAtLocation(urlImage);
            return new Patch(urlImage, getBytes(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    private InputStream getResourceAtLocation(String location) {
        try {
            return new GetCall().execute(location).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getImageUrlFromPage(InputStream page) throws IOException {
        String reader = new String(getBytes(page));
        String aux = reader.substring(reader.indexOf("cardtable-cardimage"));
        String ref = "href=\"";
        aux = aux.substring(aux.indexOf(ref) + ref.length());
        return aux.substring(0, aux.indexOf("\""));
    }
}
