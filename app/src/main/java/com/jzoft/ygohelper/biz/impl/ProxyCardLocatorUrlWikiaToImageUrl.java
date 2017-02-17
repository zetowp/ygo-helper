package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;

import java.io.IOException;

/**
 * Created by jjimenez on 11/10/16.
 */
public class ProxyCardLocatorUrlWikiaToImageUrl implements ProxyCardLocator {

    private HttpCallerFactory callerFactory;

    public ProxyCardLocatorUrlWikiaToImageUrl(HttpCallerFactory callerFactory) {
        this.callerFactory = callerFactory;
    }

    @Override
    public ProxyCard locate(String location) throws HttpCaller.NotFound {
        try {
            return new ProxyCard(getImageUrlFromPage(callerFactory.getCaller().getCall(location)), null);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getImageUrlFromPage(byte[] page) throws IOException {
        try {
            String reader = new String(page);
            String aux = reader.substring(reader.indexOf("cardtable-cardimage"));
            String ref = "href=\"";
            aux = aux.substring(aux.indexOf(ref) + ref.length());
            return aux.substring(0, aux.indexOf("\""));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean hasToLocate(String location) {
        return location.contains("http") && !location.contains(".png");
    }

}
