package com.jzoft.ygohelper.biz;

import com.jzoft.ygohelper.mock.PatchLocalizatorMock;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchHolderTest {

    private PatchHolder holder;
    private PatchLocalizatorMock localizator;

    @Before
    public void setUp() throws Exception {
        localizator = new PatchLocalizatorMock();
        holder = new PatchHolder(localizator);
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
        List<Patch> patches = holder.getPatches();
        assertEquals(patches.get(0), patches.get(1));
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
        List<Patch> patches = holder.getPatches();
        assertEquals(patches.get(0), patches.get(1));
        assertEquals(patches.get(4), patches.get(5));
    }
}
