package com.jzoft.ygohelper.biz;

import com.jzoft.ygohelper.utils.HttpCaller;

/**
 * Created by jjimenez on 11/10/16.
 */
public interface PatchLocator {
    Patch locate(String location) throws HttpCaller.NotFound;

    boolean hasToLocate(String location);

}
