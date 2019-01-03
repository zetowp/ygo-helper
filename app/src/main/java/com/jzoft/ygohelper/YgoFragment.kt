package com.jzoft.ygohelper

import android.support.v4.app.Fragment


/**
 * Created by jjimenez on 8/03/17.
 */
abstract class YgoFragment : Fragment() {
    abstract val title: String

    abstract val options: List<Int>
}
