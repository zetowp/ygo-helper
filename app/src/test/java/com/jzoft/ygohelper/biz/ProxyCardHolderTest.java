package com.jzoft.ygohelper.biz;

import com.jzoft.ygohelper.mock.PatchLocatorMock;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by jjimenez on 11/10/16.
 */
public class ProxyCardHolderTest {

    private ProxyCardHolder holder;
    private PatchLocatorMock localizator;

    @Before
    public void setUp() throws Exception {
        localizator = new PatchLocatorMock();
        holder = new ProxyCardHolder(localizator);
    }

    @Test
    public void init() throws Exception {
        assertNotNull(holder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenCopyUneistingLast_throwIllegalArgumentException() throws Exception {
        holder.copy(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAddAndNonExisting_throwIllegalArgument() throws Exception {
        String location = null;
        holder.add(location);
    }

    @Test
    public void givenAddOk_return() throws Exception {
        String location = "A Valid Location";
        localizator.addValid(location);
        holder.add(location);
    }

    @Test
    public void givenAddOkAndCopyFirst_returnSame() throws Exception {
        String location = rnd();
        localizator.addValid(location);
        holder.add(location);
        holder.copy(0);
        List<ProxyCard> proxyCards = holder.getProxyCards();
        assertEquals(proxyCards.get(0), proxyCards.get(1));
    }

    private String rnd() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void givenAllValids_copyIntermediate_copyMustBeTheSame() throws Exception {
        localizator.allValid();
        holder.add(rnd());
        holder.copy(0);
        holder.add(rnd());
        holder.add(rnd());
        holder.add(rnd());
        holder.add(rnd());
        holder.copy(4);
        List<ProxyCard> proxyCards = holder.getProxyCards();
        assertEquals(proxyCards.get(0), proxyCards.get(1));
        assertEquals(proxyCards.get(4), proxyCards.get(5));
    }
}