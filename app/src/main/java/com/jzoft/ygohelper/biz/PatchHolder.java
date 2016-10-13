package com.jzoft.ygohelper.biz;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjimenez on 11/10/16.
 */
public class PatchHolder {
    private final PatchLocalizator localizator;
    private LinkedList<Patch> patches;

    public PatchHolder(PatchLocalizator localizator) {
        this.localizator = localizator;
        patches = new LinkedList<>();
    }

    public void copy(int index) {
        try {
            patches.add(index + 1, patches.get(index));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    public void add(String location) {
        Patch patch = localizator.localize(location);
        if (patch == null)
            throw new IllegalArgumentException();
        patches.add(patch);
    }

    public void remove(int index) {
        patches.remove(index);
    }

    public List<Patch> getPatches() {
        return patches;
    }
}
