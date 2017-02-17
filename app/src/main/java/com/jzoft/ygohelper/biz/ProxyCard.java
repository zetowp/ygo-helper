package com.jzoft.ygohelper.biz;

/**
 * Created by jjimenez on 11/10/16.
 */
public class ProxyCard {
    private final byte[] image;
    private final String url;

    public ProxyCard(String url, byte[] image) {
        this.url = url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ProxyCard{" +
                "image=" + image +
                ", url='" + url + '\'' +
                '}';
    }
}
