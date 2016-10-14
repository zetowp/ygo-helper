package com.jzoft.ygohelper.utils;

import com.jzoft.ygohelper.biz.impl.GetCallAsync;

/**
 * Created by jjimenez on 14/10/16.
 */
public class HttpCallerFactory {

    public HttpCaller getCaller(){
        return new GetCallAsync();
    }
}
