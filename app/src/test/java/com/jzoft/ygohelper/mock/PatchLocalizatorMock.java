package com.jzoft.ygohelper.mock;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocalizator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchLocalizatorMock implements PatchLocalizator {

    private Map<String, String> valid = new HashMap<>();
    private boolean allValid = false;

    public void addValid(String location) {
        valid.put(location, location);
    }

    @Override
    public Patch localize(String location) {
        if (allValid || valid.containsKey(location))
            return new Patch(null, null);
        return null;
    }

    public void allValid() {
        allValid = true;
    }
}
