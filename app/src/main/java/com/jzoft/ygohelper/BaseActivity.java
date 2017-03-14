package com.jzoft.ygohelper;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jzoft.ygohelper.ui.Option;
import com.jzoft.ygohelper.ui.ToolsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.tools_layout);
        ListView list = (ListView) this.findViewById(R.id.tools_drawer);
        addTools(list);
        configureDrawer((DrawerLayout) this.findViewById(R.id.app_tools));
    }

    protected void configureDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.addDrawerListener(getDrawerListener(drawer));
    }

    @NonNull
    protected ActionBarDrawerToggle getDrawerListener(final DrawerLayout drawer) {
        return new ActionBarDrawerToggle(this, drawer, R.string.drawer_desc, R.string.drawer_desc) {

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
                return false;
            }
        };
    }

    private void addTools(ListView list) {
        List<Option> options = new ArrayList<>();
        options.add(new Option(Option.PROXY, "Proxy Maker", R.drawable.proxy_tool));
        list.setAdapter(new ToolsListAdapter(this, options));
        list.setOnItemClickListener(getToolsListener());
    }

    public AdapterView.OnItemClickListener getToolsListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Option item = (Option) adapterView.getSelectedItem();
                switch (item.getId()) {
                    case Option.PROXY:
                        startActivity(new Intent(getApplication(), ProxyCardCreatorActivity.class));
                        break;
                    default:
                        Toast.makeText(getBaseContext(), getString(R.string.notAvailable), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
