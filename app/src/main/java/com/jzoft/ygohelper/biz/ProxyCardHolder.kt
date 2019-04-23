package com.jzoft.ygohelper.biz

import com.jzoft.ygohelper.utils.Caller

import java.util.LinkedList

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCardHolder(private val localizator: ProxyCardLocator) {
    val proxyCards: MutableList<ProxyCard>

    init {
        proxyCards = LinkedList()
    }

    fun copy(index: Int) {
        try {
            proxyCards.add(index + 1, proxyCards[index])
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException()
        }

    }

    @Throws(Caller.NotFound::class)
    fun add(location: String) {
        localizator.locate(location).subscribe { proxyCards.add(it) }
    }

    fun remove(index: Int) {
        proxyCards.removeAt(index)
    }
}
