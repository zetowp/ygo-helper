package com.jzoft.ygohelper.utils;

/**
 * Created by jjimenez on 13/10/16.
 */
public interface HttpCaller {

    byte[] getCall(String url) throws NotFound;


    class NotFound extends Exception {
        private String url;

        public NotFound(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
