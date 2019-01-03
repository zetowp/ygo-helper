package com.jzoft.ygohelper

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

import com.jzoft.ygohelper.databinding.ActivityHomeYgoBinding
import com.jzoft.ygohelper.ui.Option
import com.jzoft.ygohelper.ui.ToolsListAdapter

import java.util.ArrayList

class HomeYgoActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var options: DrawerLayout? = null
    private var fragment: YgoFragment? = null
    private var menu: Menu? = null

    val toolsListener: AdapterView.OnItemClickListener
        get() = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
            val item = adapterView.adapter.getItem(position) as Option
            when (item.id) {
                Option.PROXY -> if (fragment !is ProxyCardCreatorFragment)
                    addContent(proxyContent)
                Option.CALCULATOR -> if (fragment !is DuelCalculatorFragment)
                    addContent(calculator)
                else -> Toast.makeText(baseContext, getString(R.string.notAvailable), Toast.LENGTH_LONG).show()
            }
            options!!.closeDrawer(GravityCompat.START)
        }

    private val proxyContent: YgoFragment
        get() = ProxyCardCreatorFragment()

    val calculator: YgoFragment
        get() = DuelCalculatorFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHomeYgoBinding>(this, R.layout.activity_home_ygo)
        addTools(binding.optionsList)
        options = binding.options
        configureDrawer(options!!)
        toolbar = binding.toolbar.bar
        setSupportActionBar(toolbar)
        configureToolbar(toolbar!!)
        addDefaultContent()
    }

    private fun addTools(list: ListView) {
        val options = ArrayList<Option>()
        options.add(Option(Option.PROXY, "Proxy Maker", R.drawable.proxy_tool))
        options.add(Option(Option.CALCULATOR, "Duel Calculator", R.drawable.calculator))
        list.adapter = ToolsListAdapter(this, options)
        list.onItemClickListener = toolsListener
    }

    private fun configureDrawer(drawer: DrawerLayout) {
        drawer.closeDrawer(GravityCompat.START)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawer.addDrawerListener(getDrawerListener(drawer))
    }

    private fun getDrawerListener(drawer: DrawerLayout): ActionBarDrawerToggle {
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


    private fun configureToolbar(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.drawable.menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val b = super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> if (options!!.isDrawerOpen(GravityCompat.START)) {
                options!!.closeDrawer(GravityCompat.START)
            } else {
                options!!.openDrawer(GravityCompat.START)
            }
            else -> fragment!!.onOptionsItemSelected(item)
        }
        return b
    }

    private fun addDefaultContent() {
        addContent(calculator)
    }

    private fun addContent(fragment: YgoFragment) {
        if (menu != null)
            configureMenu(fragment)
        this.fragment = fragment
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }

    private fun configureMenu(fragment: YgoFragment?) {
        if (this.fragment != null)
            enableOptions(this.fragment!!.options, false)
        enableOptions(fragment!!.options, true)
        toolbar!!.title = fragment.title
    }

    private fun enableOptions(options: List<Int>, enabled: Boolean) {
        for (opt in options) {
            enabledIfAny(menu!!.findItem(opt), enabled)
        }
    }

    private fun enabledIfAny(item: MenuItem?, enabled: Boolean) {
        if (item != null) item.isVisible = enabled
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.patches_menu, menu)
        this.menu = menu
        configureMenu(fragment)
        return super.onCreateOptionsMenu(menu)
    }
}
