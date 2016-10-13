package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchLocalizatorWikiaTest {

    private static final String TEST_URL_TO_IMAGE = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347";

    @Test
    public void givenBadPath_returnNull() throws Exception {
        assertNull(new PatchLocalizatorWikia().localize("NotAnUrl"));
    }


    @Test
    public void givenOkPath_returnPatch() throws Exception {
        Patch patch = new PatchLocalizatorWikia().localize("http://yugioh.wikia.com/wiki/Reinforcement_of_the_Army");
        assertNotNull(patch);
        assertEquals(patch.getUrl(), TEST_URL_TO_IMAGE);
    }


}
