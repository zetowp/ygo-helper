package com.jzoft.ygohelper.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.jzoft.ygohelper.R
import com.jzoft.ygohelper.databinding.OptionItemBinding

/**
 * Created by jjimenez on 17/02/17.
 */
class ToolsListAdapter(context: Context, objects: List<Any>) : ArrayAdapter<Any>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val optionsBinding = DataBindingUtil.inflate<OptionItemBinding>(inflater, R.layout.option_item, parent, false)
        val item = this.getItem(position) as Option
        optionsBinding.icon.setImageResource(item.iconId)
        optionsBinding.name.text = item.name
        return optionsBinding.root
    }

}
