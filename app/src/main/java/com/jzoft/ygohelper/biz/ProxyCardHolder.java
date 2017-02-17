package com.jzoft.ygohelper.biz;

import com.jzoft.ygohelper.utils.HttpCaller;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjimenez on 11/10/16.
 */
public class ProxyCardHolder {
    private final ProxyCardLocator localizator;
    private List<ProxyCard> proxyCards;

    public ProxyCardHolder(ProxyCardLocator localizator) {
        this.localizator = localizator;
        proxyCards = new LinkedList<ProxyCard>();
    }

    public void copy(int index) {
        try {
            proxyCards.add(index + 1, proxyCards.get(index));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    public void add(String location) throws HttpCaller.NotFound {
        ProxyCard proxyCard = localizator.locate(location);
        if (proxyCard == null)
            throw new IllegalArgumentException();
        proxyCards.add(proxyCard);
    }

    public void remove(int index) {
        proxyCards.remove(index);
    }

    public List<ProxyCard> getProxyCards() {
        return proxyCards;
    }
}
