package com.jzoft.ygohelper.adapters

import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import com.jzoft.ygohelper.R
import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardHolder
import com.jzoft.ygohelper.biz.ProxyCardLoader
import com.jzoft.ygohelper.biz.ProxyCardPrinter
import com.jzoft.ygohelper.biz.impl.*
import com.jzoft.ygohelper.gone
import com.jzoft.ygohelper.utils.HttpCaller
import com.jzoft.ygohelper.utils.HttpCallerFactory
import com.jzoft.ygohelper.utils.impl.ImageOptimizerDisplay
import com.jzoft.ygohelper.visible
import kotlinx.android.synthetic.main.card_sample.view.*

import java.io.ByteArrayInputStream
import java.io.File
import java.util.LinkedList

/**
 * Created by jjimenez on 13/10/16.
 */
class PatchAdapter(private val clipboard: ClipboardManager, private val keyboard: InputMethodManager, private val context: Context) : RecyclerView.Adapter<PatchAdapter.ViewHolder>() {

    private val list: MutableList<ProxyCard>
    private val proxyCardHolder: ProxyCardHolder
    private val loader: ProxyCardLoader
    private val printer: ProxyCardPrinter

    private val proxyFile = createProxyFile()


    private fun createProxyFile(): File {
        val root = buildLoadFile()
        if (!root.exists())
            root.mkdir()
        return File(root, "proxy.txt")
    }


    private val textFromVirtualKeyboard: String
        get() {
            keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            return "Hola mundo"
        }

    private val location: String
        @Throws(PatchAdapter.NothingOnClipboard::class)
        get() {
            if (clipboard.primaryClip != null && clipboard.primaryClip!!.itemCount > 0) {
                val itemAt = clipboard.primaryClip!!.getItemAt(0)
                return itemAt.coerceToText(context).toString()
            } else {
                throw NothingOnClipboard()
            }
        }

    init {
        val caller = HttpCallerFactory()
        val urlToImage = ProxyCardLocatorUrlToImage(caller, ImageOptimizerDisplay())
        proxyCardHolder = ProxyCardHolder(ProxyCardLocatorLinked.buildPatchLocatorLinked(
                ProxyCardLocatorWordToUrlWikia(), ProxyCardLocatorUrlWikiaToImageUrl(caller),
                urlToImage)!!)
        loader = ProxyCardLoader(proxyFile, urlToImage)
        printer = ProxyCardWebView(context)
        list = LinkedList()
        proxyCardHolder.proxyCards.addAll(loader.loadAll())
        refresh()
    }

    private fun buildLoadFile(): File {
        return File(context.filesDir, "proxys")
    }

    fun print() {
        printer.print(proxyCardHolder.proxyCards)
    }

    fun clear() {
        proxyCardHolder.proxyCards.clear()
        refresh()
    }

    private fun refresh() {
        list.clear()
        list.addAll(proxyCardHolder.proxyCards)
        loader.saveAll(proxyCardHolder.proxyCards)
        list.add(ProxyCard(null, null))
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_sample, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(proxyCard: ProxyCard, index: Int) {
            if (proxyCard.image == null) {

                itemView.copyLast.gone()
                itemView.deleteItem.gone()
                itemView.imageSample.setImageResource(R.drawable.not_found)
            } else {
                itemView.copyLast.visible()
                itemView.deleteItem.visible()
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(proxyCard.image))
                itemView.imageSample.setImageBitmap(bitmap)
            }
            addCopyListener(index)
            addDeleteListener(index)
            addPasteListener()
            itemView.find.setOnClickListener { Toast.makeText(context, "Temporaly disabled", Toast.LENGTH_SHORT) }
        }

        private fun addKeyboardListener() {
            itemView.find.setOnClickListener { Toast.makeText(context, "Temporaly disabled", Toast.LENGTH_SHORT) }
        }

        private fun addPasteListener() {
            itemView.pasteItem.setOnClickListener {
                try {
                    tryToAdd(location)
                } catch (e: NothingOnClipboard) {
                    Toast.makeText(context, "Nothing in the clipboard", Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun tryToAdd(location: String) {
            try {
                proxyCardHolder.add(location)
                refresh()
            } catch (notFound: HttpCaller.NotFound) {
                Toast.makeText(context, "Not Found: " + notFound.url, Toast.LENGTH_LONG).show()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, "Entry error:  $location", Toast.LENGTH_LONG).show()
            }

        }

        private fun addDeleteListener(position: Int) {
            itemView.deleteItem.setOnClickListener {
                proxyCardHolder.remove(position)
                refresh()
            }
        }

        private fun addCopyListener(position: Int) {
            itemView.copyLast.setOnClickListener {
                proxyCardHolder.copy(position)
                refresh()
            }
        }
    }

    class NothingOnClipboard : Exception()
}
