package com.jzoft.ygohelper

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

import com.jzoft.ygohelper.ui.Option
import com.jzoft.ygohelper.ui.ToolsListAdapter

import java.util.ArrayList

open class BaseActivity : AppCompatActivity() {

    val toolsListener: AdapterView.OnItemClickListener
        get() = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.selectedItem as Option
            when (item.id) {
                Option.PROXY -> startActivity(Intent(application, ProxyCardCreatorActivity::class.java))
                else -> Toast.makeText(baseContext, getString(R.string.notAvailable), Toast.LENGTH_LONG).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.tools_layout)
        val list = this.findViewById<View>(R.id.tools_drawer) as ListView
        addTools(list)
        configureDrawer(this.findViewById<View>(R.id.app_tools) as DrawerLayout)
    }

    protected fun configureDrawer(drawer: DrawerLayout) {
        drawer.closeDrawer(GravityCompat.START)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawer.addDrawerListener(getDrawerListener(drawer))
    }

    protected fun getDrawerListener(drawer: DrawerLayout): ActionBarDrawerToggle {
        return object : ActionBarDrawerToggle(this, drawer, R.string.drawer_desc, R.string.drawer_desc) {

            override fun onOptionsItemSelected(item: MenuItem?): Boolean {
                if (item != null && item.itemId == android.R.id.home) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START)
                    } else {
                        drawer.openDrawer(GravityCompat.START)
                    }
                }
                return false
            }
        }
    }

    private fun addTools(list: ListView) {
        val options = ArrayList<Option>()
        options.add(Option(Option.PROXY, "Proxy Maker", R.drawable.proxy_tool))
        list.adapter = ToolsListAdapter(this, options)
        list.onItemClickListener = toolsListener
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}
