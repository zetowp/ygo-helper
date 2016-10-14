package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocator;
import com.jzoft.ygohelper.utils.HttpCaller;

/**
 * Created by jjimenez on 13/10/16.
 */
public class PatchLocatorLinked implements PatchLocator {

    private PatchLocator locator;
    private PatchLocator next;

    private PatchLocatorLinked(PatchLocator locator, PatchLocator next) {
        this.locator = locator;
        this.next = next;
    }

    @Override
    public Patch locate(String location) throws HttpCaller.NotFound {
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

    public static PatchLocator buildPatchLocatorLinked(PatchLocator... locators) {
        if (locators.length == 0)
            return null;
        return new PatchLocatorLinked(locators[0], buildPatchLocatorLinked(removeFirst(locators)));
    }

    private static PatchLocator[] removeFirst(PatchLocator[] locators) {
        PatchLocator[] withoutFirst = new PatchLocator[locators.length - 1];
        for (int i = 1; i < locators.length; i++) {
            withoutFirst[i - 1] = locators[i];
        }
        return withoutFirst;
    }
}
