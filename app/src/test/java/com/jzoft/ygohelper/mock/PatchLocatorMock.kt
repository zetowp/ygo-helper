package com.jzoft.ygohelper.mock

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator

import java.util.HashMap

/**
 * Created by jjimenez on 11/10/16.
 */
class PatchLocatorMock : ProxyCardLocator {

    private val valid = HashMap<String, String>()
    private var allValid = false

    fun addValid(location: String) {
        valid[location] = location
    }

    override fun locate(location: String): ProxyCard? {
        return if (allValid || valid.containsKey(location)) ProxyCard(null, null) else null
    }

    override fun hasToLocate(location: String): Boolean {
        return false
    }

    fun allValid() {
        allValid = true
    }
}
