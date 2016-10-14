package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocator;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;

import java.io.IOException;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchLocatorUrlWikiaToImageUrl implements PatchLocator {

    private HttpCallerFactory callerFactory;

    public PatchLocatorUrlWikiaToImageUrl(HttpCallerFactory callerFactory) {
        this.callerFactory = callerFactory;
    }

    @Override
    public Patch locate(String location) throws HttpCaller.NotFound {
        try {
            return new Patch(getImageUrlFromPage(callerFactory.getCaller().getCall(location)), null);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getImageUrlFromPage(byte[] page) throws IOException {
        String reader = new String(page);
        String aux = reader.substring(reader.indexOf("cardtable-cardimage"));
        String ref = "href=\"";
        aux = aux.substring(aux.indexOf(ref) + ref.length());
        return aux.substring(0, aux.indexOf("\""));
    }

    @Override
    public boolean hasToLocate(String location) {
        return location.contains("http") && !location.contains(".png");
    }

}
