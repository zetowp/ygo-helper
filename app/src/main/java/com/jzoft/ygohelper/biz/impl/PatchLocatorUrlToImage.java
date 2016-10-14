package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocator;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;
import com.jzoft.ygohelper.utils.ImageOptimizer;

/**
 * Created by jjimenez on 13/10/16.
 */
public class PatchLocatorUrlToImage implements PatchLocator {

    private ImageOptimizer optimizer;
    private HttpCallerFactory callerFactory;

    public PatchLocatorUrlToImage(HttpCallerFactory callerFactory, ImageOptimizer optimizer) {
        this.callerFactory = callerFactory;
        this.optimizer = optimizer;
    }

    @Override
    public Patch locate(String location) throws HttpCaller.NotFound {
        return new Patch(location, optimizer.optimiceImage(callerFactory.getCaller().getCall(location)));
    }

    @Override
    public boolean hasToLocate(String location) {
        return true;
    }
}
