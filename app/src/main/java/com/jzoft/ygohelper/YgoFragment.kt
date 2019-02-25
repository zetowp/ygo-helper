package com.jzoft.ygohelper

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by jjimenez on 8/03/17.
 */
abstract class YgoFragment : Fragment() {

    abstract val title: String
    abstract val options: List<Int>
    abstract val fragmentLayout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(fragmentLayout, container, false)
    }

}
