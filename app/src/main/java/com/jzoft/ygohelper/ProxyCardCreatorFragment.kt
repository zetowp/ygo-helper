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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import com.jzoft.ygohelper.adapters.PatchAdapter
import com.jzoft.ygohelper.databinding.ActivityPatchCreatorBinding
import com.jzoft.ygohelper.utils.ActivityUtils

import java.util.Arrays

import android.content.Context.CLIPBOARD_SERVICE

class ProxyCardCreatorFragment : YgoFragment() {
    private val tools: DrawerLayout? = null

    private var patchAdapter: PatchAdapter? = null
    private lateinit var act: Activity

    override val options: List<Int>
        get() = Arrays.asList(R.id.print_patches, R.id.clear, R.id.info)

    private val okClearAction: DialogInterface.OnClickListener
        get() = DialogInterface.OnClickListener { dialog, id ->
            patchAdapter!!.clear()
            dialog.dismiss()
        }


    override val title: String
        get() = "Proxy Maker"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        act = activity as Activity
        verifyStoragePermissions(act)
        val binding = DataBindingUtil.inflate<ActivityPatchCreatorBinding>(inflater, R.layout.activity_patch_creator, container, false)
        val keyboard = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        patchAdapter = PatchAdapter(act.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager, keyboard, act)
        binding.patchList.adapter = patchAdapter
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.print_patches -> patchAdapter!!.print()
            R.id.clear -> ActivityUtils.okSimpleCancelAlert(createCleearAlertHolder(), okClearAction)
            R.id.info -> ActivityUtils.simpleAlert(context!!, INFO_PROXY)
            else -> {
            }
        }
        return true
    }

    private fun createCleearAlertHolder(): ActivityUtils.AlertHolder {
        return ActivityUtils.AlertHolder("Do you really want to clear all the proxys?",
                "Delete it!", "Not this time", context!!)
    }

    companion object {


        private val REQUEST_EXTERNAL_STORAGE = 1
        private val INFO_PROXY = "Proxy Maker Instructions:\n" +
                "On the image:\n" +
                "- the paste you can paste an allready copied url from wikia of the card you want to make a proxy, or the url of the image.\n" +
                "- copy will create a copy of the proxy\n" +
                "- X To delete\n" +
                "\n" +
                "Menu\n" +
                "- Trash can will delete all of your proxys\n" +
                "- Print will create a html that you can send to yous computer (e.g. via Email), open it with Chrome or other browser and print it.\n"

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
