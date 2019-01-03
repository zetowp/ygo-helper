package com.jzoft.ygohelper.biz

import com.jzoft.ygohelper.utils.HttpCaller

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

    @Throws(HttpCaller.NotFound::class)
    fun add(location: String) {
        val proxyCard = localizator.locate(location) ?: throw IllegalArgumentException()
        proxyCards.add(proxyCard)
    }

    fun remove(index: Int) {
        proxyCards.removeAt(index)
    }
}
