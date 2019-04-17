package com.jzoft.ygohelper.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jzoft.ygohelper.R
import kotlinx.android.synthetic.main.option_item.view.*

/**
 * Created by jjimenez on 17/02/17.
 */
class ToolsListAdapter(context: Context, objects: List<Option>) : ArrayAdapter<Option>(context, R.layout.option_item, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView =(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.option_item, null)
        val option = getItem(position)!!
        itemView.icon.setImageResource(option.iconId)
        itemView.name.text = option.name
        itemView.setOnClickListener { option.onClick.invoke() }
        return itemView
    }

}
