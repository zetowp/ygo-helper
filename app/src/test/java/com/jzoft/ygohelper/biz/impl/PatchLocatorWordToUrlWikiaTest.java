package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocator;

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
public class PatchLocatorWordToUrlWikiaTest {

    private PatchLocator locator;

    @Before
    public void setUp() throws Exception {
        locator = new PatchLocatorWordToUrlWikia();
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
        Patch patch = locator.locate("reinforcement of the army");
        assertNotNull(patch);
        assertEquals(patch.getUrl(), realLocation);
        assertNull(patch.getImage());
    }

}
