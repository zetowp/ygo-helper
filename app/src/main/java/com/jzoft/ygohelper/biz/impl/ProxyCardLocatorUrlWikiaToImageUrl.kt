package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.HttpCaller
import com.jzoft.ygohelper.utils.HttpCallerFactory

import java.io.IOException

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCardLocatorUrlWikiaToImageUrl(private val callerFactory: HttpCallerFactory) : ProxyCardLocator {

    @Throws(HttpCaller.NotFound::class)
    override fun locate(location: String): ProxyCard {
        try {
            return ProxyCard(getImageUrlFromPage(callerFactory.caller.getCall(location)), null)
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
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
