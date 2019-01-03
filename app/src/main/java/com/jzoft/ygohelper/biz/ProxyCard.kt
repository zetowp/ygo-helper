package com.jzoft.ygohelper.biz

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCard(val url: String?, val image: ByteArray?) {

    override fun toString(): String {
        return "ProxyCard{" +
                "image=" + image +
                ", url='" + url + '\''.toString() +
                '}'.toString()
    }
}
