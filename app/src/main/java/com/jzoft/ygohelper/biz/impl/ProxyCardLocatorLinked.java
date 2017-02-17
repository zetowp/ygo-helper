package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;
import com.jzoft.ygohelper.utils.HttpCaller;

/**
 * Created by jjimenez on 13/10/16.
 */
public class ProxyCardLocatorLinked implements ProxyCardLocator {

    private ProxyCardLocator locator;
    private ProxyCardLocator next;

    private ProxyCardLocatorLinked(ProxyCardLocator locator, ProxyCardLocator next) {
        this.locator = locator;
        this.next = next;
    }

    @Override
    public ProxyCard locate(String location) throws HttpCaller.NotFound {
        if (!hasToLocate(location) && next != null)
            return next.locate(location);
        if (next == null)
            return locator.locate(location);
        else
            return next.locate(locator.locate(location).getUrl());
    }

    @Override
    public boolean hasToLocate(String location) {
        return locator.hasToLocate(location);
    }

    public static ProxyCardLocator buildPatchLocatorLinked(ProxyCardLocator... locators) {
        if (locators.length == 0)
            return null;
        return new ProxyCardLocatorLinked(locators[0], buildPatchLocatorLinked(removeFirst(locators)));
    }

    private static ProxyCardLocator[] removeFirst(ProxyCardLocator[] locators) {
        ProxyCardLocator[] withoutFirst = new ProxyCardLocator[locators.length - 1];
        for (int i = 1; i < locators.length; i++) {
            withoutFirst[i - 1] = locators[i];
        }
        return withoutFirst;
    }
}
