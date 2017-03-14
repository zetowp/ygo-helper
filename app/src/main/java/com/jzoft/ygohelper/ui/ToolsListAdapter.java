package com.jzoft.ygohelper.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jzoft.ygohelper.R;
import com.jzoft.ygohelper.databinding.OptionItemBinding;

import java.util.List;

/**
 * Created by jjimenez on 17/02/17.
 */
public class ToolsListAdapter extends ArrayAdapter {

    public ToolsListAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OptionItemBinding optionsBinding = DataBindingUtil.inflate(inflater, R.layout.option_item, parent, false);
        Option item = (Option) this.getItem(position);
        optionsBinding.icon.setImageResource(item.getIconId());
        optionsBinding.name.setText(item.getName());
        return optionsBinding.getRoot();
    }

}
