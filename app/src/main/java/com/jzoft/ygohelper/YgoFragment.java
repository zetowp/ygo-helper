package com.jzoft.ygohelper;

import android.app.Fragment;

import java.util.List;

/**
 * Created by jjimenez on 8/03/17.
 */
public abstract class YgoFragment extends Fragment {
    public abstract String getTitle();

    public abstract List<Integer> getOptions();
}
