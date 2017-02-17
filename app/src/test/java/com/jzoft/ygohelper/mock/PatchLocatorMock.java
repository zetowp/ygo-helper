package com.jzoft.ygohelper.mock;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchLocatorMock implements ProxyCardLocator {

    private Map<String, String> valid = new HashMap<>();
    private boolean allValid = false;

    public void addValid(String location) {
        valid.put(location, location);
    }

    @Override
    public ProxyCard locate(String location) {
        if (allValid || valid.containsKey(location))
            return new ProxyCard(null, null);
        return null;
    }

    @Override
    public boolean hasToLocate(String location) {
        return false;
    }

    public void allValid() {
        allValid = true;
    }
}
