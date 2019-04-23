package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.utils.impl.CallerFactoryHttp
import rx.Observable

import java.io.IOException

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCardLocatorUrlWikiaToImageUrl(private val callerFactory: CallerFactoryHttp) : ProxyCardLocator {

    @Throws(Caller.NotFound::class)
    override fun locate(location: String): Observable<ProxyCard> {
        return callerFactory.createCaller().getCall(location).flatMap {
             Observable.just(ProxyCard(getImageUrlFromPage(it), null))
        }
    }

    @Throws(IOException::class)
    private fun getImageUrlFromPage(page: ByteArray): String {
        try {
            val reader = String(page)
            var aux = reader.substring(reader.indexOf("cardtable-cardimage"))
            val ref = "href=\""
            aux = aux.substring(aux.indexOf(ref) + ref.length)
            return aux.substring(0, aux.indexOf("\""))
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }

    }

    override fun hasToLocate(location: String): Boolean {
        return location.contains("http") && !location.contains(".png")
    }

}
