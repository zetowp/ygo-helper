package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.HttpCaller
import com.jzoft.ygohelper.utils.HttpCallerFactory

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue

/**
 * Created by jjimenez on 13/10/16.
 */
class ProxyCardLocatorUrlWikiaToImageUrlTest {
    private var locator: ProxyCardLocator? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        locator = ProxyCardLocatorUrlWikiaToImageUrl(HttpCallerFactory())
    }

    @Test(expected = HttpCaller.NotFound::class)
    @Throws(Exception::class)
    fun givenBadPath_returnNull() {
        val location = "NotAnUrl"
        assertFalse(locator!!.hasToLocate(location))
        locator!!.locate("http://NotAnUrl")
    }


    @Test
    @Throws(Exception::class)
    fun givenOkPath_returnPatch() {
        val location = "http://yugioh.wikia.com/wiki/Reinforcement_of_the_Army"
        assertTrue(locator!!.hasToLocate(location))
        val proxyCard = locator!!.locate(location)
        assertNotNull(proxyCard)
        assertEquals(proxyCard.url, TEST_URL_TO_IMAGE)
        assertNull(proxyCard.image)
    }

    companion object {


        private val TEST_URL_TO_IMAGE = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347"
    }


}
