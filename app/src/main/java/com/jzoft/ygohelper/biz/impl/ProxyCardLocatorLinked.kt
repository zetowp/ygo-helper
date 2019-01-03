package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.HttpCaller

/**
 * Created by jjimenez on 13/10/16.
 */
class ProxyCardLocatorLinked private constructor(private val locator: ProxyCardLocator, private val next: ProxyCardLocator?) : ProxyCardLocator {

    @Throws(HttpCaller.NotFound::class)
    override fun locate(location: String): ProxyCard {
        if (!hasToLocate(location) && next != null)
            return next.locate(location)
        return if (next == null)
            locator.locate(location)
        else
            next.locate(locator.locate(location).url!!)
    }

    override fun hasToLocate(location: String): Boolean {
        return locator.hasToLocate(location)
    }

    companion object {

        fun buildPatchLocatorLinked(vararg locators: ProxyCardLocator): ProxyCardLocator? {
            return if (locators.isEmpty()) null else ProxyCardLocatorLinked(locators[0], buildPatchLocatorLinked(*removeFirst(locators)))
        }

        private fun removeFirst(locators: Array<out ProxyCardLocator>): Array<ProxyCardLocator> {
            val withoutFirst = mutableListOf<ProxyCardLocator>()
            for (i in 1 until locators.size) {
                withoutFirst.add(locators[i])
            }
            return withoutFirst.toTypedArray()
        }
    }
}
