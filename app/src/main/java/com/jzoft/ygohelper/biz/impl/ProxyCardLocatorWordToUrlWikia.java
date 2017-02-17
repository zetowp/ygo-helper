package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jjimenez on 11/10/16.
 */
public class ProxyCardLocatorWordToUrlWikia implements ProxyCardLocator {

    private static final Map<String, String> nonCapitalize = buildNonCapitalized();

    private static Map<String, String> buildNonCapitalized() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("of", "of");
        map.put("the", "the");
        map.put("is", "is");
        return map;
    }

    @Override
    public ProxyCard locate(String location) {
        StringBuilder builder = new StringBuilder("http://yugioh.wikia.com/wiki/");
        String[] split = location.split(" ");
        for (int i = 0; i < split.length; i++) {
            builder.append(getWordToUrl(split[i], i));
            builder.append("_");
        }
        builder.setLength(builder.length() - 1);
        return new ProxyCard(builder.toString(), null);
    }

    private String getWordToUrl(String s, int index) {
        return (index == 0 || !nonCapitalize.containsKey(s.toLowerCase())) ? capitalize(s) : s.toLowerCase();
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
    }

    @Override
    public boolean hasToLocate(String location) {
        return !location.contains("http") && (location.contains(" ") || location.length() < 30);
    }


}
