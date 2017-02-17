package com.jzoft.ygohelper.biz.impl;

import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardLocator;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;
import com.jzoft.ygohelper.utils.ImageOptimizer;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by jjimenez on 13/10/16.
 */
public class ProxyCardLocatorUrlToImageTest {


    private static final String TEST_URL_TO_IMAGE = "http://vignette4.wikia.nocookie.net/yugioh/images/e/e7/ReinforcementoftheArmy-SR02-EN-C-1E.png/revision/latest?cb=20160706183347";
    private ProxyCardLocator locator;

    @Before
    public void setUp() throws Exception {
        locator = new ProxyCardLocatorUrlToImage(new HttpCallerFactory(), new ImageOptimizer() {
            @Override
            public byte[] optimizeImage(byte[] image) {
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
        ProxyCard proxyCard = locator.locate(location);
        assertNotNull(proxyCard);
        assertEquals(proxyCard.getUrl(), TEST_URL_TO_IMAGE);
        assertNotNull(proxyCard.getImage());
        assertEquals(0, proxyCard.getImage().length);
    }


}
