package com.jzoft.ygohelper.biz;

/**
 * Created by jjimenez on 11/10/16.
 */
public class Patch {
    private final byte[] image;
    private final String url;

    public Patch(String url, byte[] image) {
        this.url = url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public byte[] getImage() {
        return image;
    }
}
