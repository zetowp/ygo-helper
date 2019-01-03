package com.jzoft.ygohelper.biz

import com.jzoft.ygohelper.mock.PatchLocatorMock

import org.junit.Before
import org.junit.Test
import java.util.UUID

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull

/**
 * Created by jjimenez on 11/10/16.
 */
class ProxyCardHolderTest {

    private var holder: ProxyCardHolder? = null
    private var localizator: PatchLocatorMock? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        localizator = PatchLocatorMock()
        holder = ProxyCardHolder(localizator!!)
    }

    @Test
    @Throws(Exception::class)
    fun init() {
        assertNotNull(holder)
    }

    @Test(expected = IllegalArgumentException::class)
    @Throws(Exception::class)
    fun givenCopyUneistingLast_throwIllegalArgumentException() {
        holder!!.copy(0)
    }

    @Test
    @Throws(Exception::class)
    fun givenAddOk_return() {
        val location = "A Valid Location"
        localizator!!.addValid(location)
        holder!!.add(location)
    }

    @Test
    @Throws(Exception::class)
    fun givenAddOkAndCopyFirst_returnSame() {
        val location = rnd()
        localizator!!.addValid(location)
        holder!!.add(location)
        holder!!.copy(0)
        val proxyCards = holder!!.proxyCards
        assertEquals(proxyCards[0], proxyCards[1])
    }

    private fun rnd(): String {
        return UUID.randomUUID().toString()
    }

    @Test
    @Throws(Exception::class)
    fun givenAllValids_copyIntermediate_copyMustBeTheSame() {
        localizator!!.allValid()
        holder!!.add(rnd())
        holder!!.copy(0)
        holder!!.add(rnd())
        holder!!.add(rnd())
        holder!!.add(rnd())
        holder!!.add(rnd())
        holder!!.copy(4)
        val proxyCards = holder!!.proxyCards
        assertEquals(proxyCards[0], proxyCards[1])
        assertEquals(proxyCards[4], proxyCards[5])
    }
}
