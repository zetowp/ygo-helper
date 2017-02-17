package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;
import com.jzoft.ygohelper.utils.ImageOptimizer;

/**
 * Created by jjimenez on 13/10/16.
 */
public class ProxyCardLocatorUrlToImage implements ProxyCardLocator {

    private ImageOptimizer optimizer;
    private HttpCallerFactory callerFactory;

    public ProxyCardLocatorUrlToImage(HttpCallerFactory callerFactory, ImageOptimizer optimizer) {
        this.callerFactory = callerFactory;
        this.optimizer = optimizer;
    }

    @Override
    public ProxyCard locate(String location) throws HttpCaller.NotFound {
        return new ProxyCard(location, optimizer.optimizeImage(callerFactory.getCaller().getCall(location)));
    }

    @Override
    public boolean hasToLocate(String location) {
        return true;
    }
}
