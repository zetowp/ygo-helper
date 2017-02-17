package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by jjimenez on 11/10/16.
 */
public class ProxyCardLocatorWordToUrlWikiaTest {

    private ProxyCardLocator locator;

    @Before
    public void setUp() throws Exception {
        locator = new ProxyCardLocatorWordToUrlWikia();
    }

    @Test
    public void givenBadPath_returnNull() throws Exception {
        assertFalse(locator.hasToLocate("http://NotAnUrl"));
        assertFalse(locator.hasToLocate("estaEsUnaPalabraMuyMuyGrandeComoParaSerUnaSolaPlabra"));
    }


    @Test
    public void givenOkPath_returnPatch() throws Exception {
        String realLocation = "http://yugioh.wikia.com/wiki/Reinforcement_of_the_Army";
        assertTrue(locator.hasToLocate("reinforcement of the army"));
        ProxyCard proxyCard = locator.locate("reinforcement of the army");
        assertNotNull(proxyCard);
        assertEquals(proxyCard.getUrl(), realLocation);
        assertNull(proxyCard.getImage());
    }

}
