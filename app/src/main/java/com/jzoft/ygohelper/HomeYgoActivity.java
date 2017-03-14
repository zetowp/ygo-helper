package com.jzoft.ygohelper;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jzoft.ygohelper.databinding.ActivityHomeYgoBinding;
import com.jzoft.ygohelper.ui.Option;
import com.jzoft.ygohelper.ui.ToolsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeYgoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout options;
    private YgoFragment fragment;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeYgoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home_ygo);
        addTools(binding.optionsList);
        options = binding.options;
        configureDrawer(options);
        toolbar = binding.toolbar.bar;
        setSupportActionBar(toolbar);
        configureToolbar(toolbar);
        addDefaultContent();
    }

    private void addTools(ListView list) {
        List<Option> options = new ArrayList<>();
        options.add(new Option(Option.PROXY, "Proxy Maker", R.drawable.proxy_tool));
        options.add(new Option(Option.CALCULATOR, "Duel Calculator", R.drawable.calculator));
        list.setAdapter(new ToolsListAdapter(this, options));
        list.setOnItemClickListener(getToolsListener());
    }

    public AdapterView.OnItemClickListener getToolsListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Option item = (Option) adapterView.getAdapter().getItem(position);
                switch (item.getId()) {
                    case Option.PROXY:
                        if (!(fragment instanceof ProxyCardCreatorFragment))
                            addContent(getProxyContent());
                        break;
                    case Option.CALCULATOR:
                        if (!(fragment instanceof DuelCalculatorFragment))
                            addContent(getCalculator());
                        break;
                    default:
                        Toast.makeText(getBaseContext(), getString(R.string.notAvailable), Toast.LENGTH_LONG).show();
                        break;
                }
                options.closeDrawer(GravityCompat.START);
            }
        };
    }

    private void configureDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.addDrawerListener(getDrawerListener(drawer));
    }

    private ActionBarDrawerToggle getDrawerListener(final DrawerLayout drawer) {
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


    private void configureToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (options.isDrawerOpen(GravityCompat.START)) {
                    options.closeDrawer(GravityCompat.START);
                } else {
                    options.openDrawer(GravityCompat.START);
                }
                break;
            default:
                fragment.onOptionsItemSelected(item);
                break;
        }
        return b;
    }

    private void addDefaultContent() {
        addContent(getCalculator());
    }

    private YgoFragment getProxyContent() {
        return new ProxyCardCreatorFragment();
    }

    private void addContent(YgoFragment fragment) {
        if (menu != null)
            configureMenu(fragment);
        this.fragment = fragment;
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void configureMenu(YgoFragment fragment) {
        if (this.fragment != null)
            enableOptions(this.fragment.getOptions(), false);
        enableOptions(fragment.getOptions(), true);
        toolbar.setTitle(fragment.getTitle());
    }

    private void enableOptions(List<Integer> options, boolean enabled) {
        for (Integer opt : options) {
            enabledIfAny(menu.findItem(opt), enabled);
        }
    }

    private void enabledIfAny(MenuItem item, boolean enabled) {
        if (item != null) item.setVisible(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patches_menu, menu);
        this.menu = menu;
        configureMenu(fragment);
        return super.onCreateOptionsMenu(menu);
    }

    public YgoFragment getCalculator() {
        return new DuelCalculatorFragment();
    }
}
