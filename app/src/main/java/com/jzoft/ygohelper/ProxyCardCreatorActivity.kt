package com.jzoft.ygohelper

import android.Manifest
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager

import com.jzoft.ygohelper.adapters.PatchAdapter
import com.jzoft.ygohelper.databinding.ActivityPatchCreatorBinding

class ProxyCardCreatorActivity : BaseActivity() {
    private val tools: DrawerLayout? = null

    private var patchAdapter: PatchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyStoragePermissions(this)
        val binding: ActivityPatchCreatorBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patch_creator)
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        patchAdapter = PatchAdapter(getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager, keyboard, this)
        binding.patchList.adapter = patchAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.patches_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.print_patches -> patchAdapter!!.print()
            R.id.clear -> askToClear()
            else -> {
            }
        }
        return true
    }

    private fun askToClear() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you really want to clear all the proxys?")
        builder.setPositiveButton("Delete it!") { dialog, id ->
            patchAdapter!!.clear()
            dialog.dismiss()
        }
        builder.setNegativeButton("Not this time") { dialog, id -> }
        val alert = builder.create()
        alert.show()
    }

    companion object {


        private val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        fun verifyStoragePermissions(activity: Activity) {
            val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }


}
