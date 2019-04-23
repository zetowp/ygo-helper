package com.jzoft.ygohelper.biz.impl

import com.jzoft.ygohelper.biz.ProxyCardLocator
import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.utils.impl.CallerFactoryHttp
import com.jzoft.ygohelper.utils.ImageOptimizer

import org.junit.Before
import org.junit.Test

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue

/**
 * Created by jjimenez on 13/10/16.
 */
class ProxyCardLocatorUrlToImageTest {
    private var locator: ProxyCardLocator? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        locator = ProxyCardLocatorUrlToImage(CallerFactoryHttp(), object : ImageOptimizer {
            override fun optimizeImage(image: ByteArray): ByteArray {
                return ByteArray(0)
            }
        })
    }

    @Test(expected = Caller.NotFound::class)
    @Throws(Exception::class)
    fun givenBadPath_returnNull() {
        val location = "NotAnUrl"
        assertTrue(locator!!.hasToLocate(location))
        locator!!.locate("http://NotAnUrl")
    }


    @Test
    @Throws(Exception::class)
    fun givenOkPath_returnPatch() {
        val location = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347"
        assertTrue(locator!!.hasToLocate(location))
        val proxyCard = locator!!.locate(location)
        assertNotNull(proxyCard)
        assertEquals(proxyCard.url, TEST_URL_TO_IMAGE)
        assertNotNull(proxyCard.image)
        assertEquals(0, proxyCard.image?.size)
    }

    companion object {


        private val TEST_URL_TO_IMAGE = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347"
    }


}
