package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import rx.Observable

import java.util.HashMap

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCardLocatorWordToUrlWikia : ProxyCardLocator {

    override fun locate(location: String): Observable<ProxyCard> {
        val builder = StringBuilder("http://yugioh.fandom.com/wiki/")
        val split = location.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (i in split.indices) {
            builder.append(getWordToUrl(split[i], i))
            builder.append("_")
        }
        builder.setLength(builder.length - 1)
        return Observable.just(ProxyCard(builder.toString(), null))
    }

    private fun getWordToUrl(s: String, index: Int): String {
        return if (index == 0 || !nonCapitalize.containsKey(s.toLowerCase())) capitalize(s) else s.toLowerCase()
    }

    private fun capitalize(line: String): String {
        return Character.toUpperCase(line[0]) + line.substring(1).toLowerCase()
    }

    override fun hasToLocate(location: String): Boolean {
        return !location.contains("http") && (location.contains(" ") || location.length < 30)
    }

    companion object {

        private val nonCapitalize = buildNonCapitalized()

        private fun buildNonCapitalized(): Map<String, String> {

            val map = HashMap<String, String>()
            map["of"] = "of"
            map["the"] = "the"
            map["is"] = "is"
            return map
        }
    }


}
