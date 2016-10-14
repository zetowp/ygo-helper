package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchLocator;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.ImageOptimizer;
import com.jzoft.ygohelper.utils.impl.HttpCallerConnection;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by jjimenez on 13/10/16.
 */
public class PatchLocatorUrlToImageTest {


    private static final String TEST_URL_TO_IMAGE = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347";
    private PatchLocator locator;

    @Before
    public void setUp() throws Exception {
        locator = new PatchLocatorUrlToImage(new HttpCallerConnection(), new ImageOptimizer() {
            @Override
            public byte[] optimiceImage(byte[] image) {
                return new byte[0];
            }
        });
    }

    @Test(expected = HttpCaller.NotFound.class)
    public void givenBadPath_returnNull() throws Exception {
        String location = "NotAnUrl";
        assertTrue(locator.hasToLocate(location));
        locator.locate("http://NotAnUrl");
    }


    @Test
    public void givenOkPath_returnPatch() throws Exception {
        String location = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347";
        assertTrue(locator.hasToLocate(location));
        Patch patch = locator.locate(location);
        assertNotNull(patch);
        assertEquals(patch.getUrl(), TEST_URL_TO_IMAGE);
        assertNotNull(patch.getImage());
        assertEquals(0, patch.getImage().length);
    }


}
