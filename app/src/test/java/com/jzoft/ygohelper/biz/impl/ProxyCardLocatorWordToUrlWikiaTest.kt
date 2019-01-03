package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCardLocatorWordToUrlWikiaTest {

    private var locator: ProxyCardLocator? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        locator = ProxyCardLocatorWordToUrlWikia()
    }

    @Test
    @Throws(Exception::class)
    fun givenBadPath_returnNull() {
        assertFalse(locator!!.hasToLocate("http://NotAnUrl"))
        assertFalse(locator!!.hasToLocate("estaEsUnaPalabraMuyMuyGrandeComoParaSerUnaSolaPlabra"))
    }


    @Test
    @Throws(Exception::class)
    fun givenOkPath_returnPatch() {
        val realLocation = "http://yugioh.wikia.com/wiki/Reinforcement_of_the_Army"
        assertTrue(locator!!.hasToLocate("reinforcement of the army"))
        val proxyCard = locator!!.locate("reinforcement of the army")
        assertNotNull(proxyCard)
        assertEquals(proxyCard.url, realLocation)
        assertNull(proxyCard.image)
    }

}
