package com.jzoft.ygohelper.biz

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCard(var url: String? = null, var image: ByteArray? = null, var save: Boolean = false) {

    override fun toString(): String {
        return "ProxyCard{" +
                "image=" + image +
                ", url='" + url + '\''.toString() +
                '}'.toString()
    }
}
