package com.jzoft.ygohelper.adapters

import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.appKodein

import com.jzoft.ygohelper.R
import com.jzoft.ygohelper.biz.*
import com.jzoft.ygohelper.gone
import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.visible
import kotlinx.android.synthetic.main.card_sample.view.*

import java.io.ByteArrayInputStream
import java.io.File

/**
 * Created by jjimenez on 13/10/16.
 */
class PatchAdapter(private val clipboard: ClipboardManager, private val keyboard: InputMethodManager, private val context: Context) : RecyclerView.Adapter<PatchAdapter.ProxyViewHolder>() {

    val kodein = LazyKodein(context.appKodein)


    private val list: MutableList<ProxyCard>
    private val loader: ProxyCardLoader
    private val printer: ProxyCardPrinter
    private val localizator: ProxyCardLocator

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
        loader = ProxyCardLoader(proxyFile)
        list = loader.loadAll().toMutableList()
        list.add(ProxyCard())
        printer = kodein().with { context }.instance()
        localizator = kodein().instance()
        refresh()
    }

    private fun buildLoadFile(): File {
        return File(context.filesDir, "proxys")
    }

    fun print() {
        printer.print(list.filter { it.url != null })
    }

    fun clear() {
        list.clear()
        list.add(ProxyCard())
        refresh()
    }

    private fun refresh() {
        saveList()
        notifyDataSetChanged()
    }

    private fun saveList() {
        loader.saveAll(list.filter { it.image != null })
    }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProxyViewHolder {
        return ProxyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_sample, parent, false))
    }

    override fun onBindViewHolder(holder: ProxyViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    inner class ProxyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(proxyCard: ProxyCard, index: Int) {
            if (proxyCard.url == null) {
                itemView.copyLast.gone()
                itemView.deleteItem.gone()
                itemView.imageSample.setImageResource(R.drawable.not_found)
            } else {
                itemView.copyLast.visible()
                itemView.deleteItem.visible()
                if (proxyCard.image == null) download(proxyCard)
                else setImage(proxyCard)
            }
            addCopyListener(index)
            addDeleteListener(index)
            addPasteListener()
            itemView.find.setOnClickListener { Toast.makeText(context, "Temporaly disabled", Toast.LENGTH_SHORT).show() }
        }

        private fun setImage(proxyCard: ProxyCard) {
            itemView.imageSample.setImageBitmap(BitmapFactory.decodeStream(ByteArrayInputStream(proxyCard.image)))
        }

        private fun download(proxyCard: ProxyCard) {
            itemView.imageSample.setImageResource(R.drawable.downloading)
            locateCard(proxyCard, location)
        }

        private fun locateCard(card: ProxyCard, location: String) {
            localizator.locate(card.url!!).subscribe({
                card.url = it.url
                card.image = it.image
                setImage(card)
                saveList()
            }, {
                when (it) {
                    is Caller.NotFound -> Toast.makeText(context, "Not Found: " + it.url, Toast.LENGTH_LONG).show()
                    else -> {
                        Toast.makeText(context, "Entry error:  $location", Toast.LENGTH_LONG).show()
                        Log.e("ERROR_IMAGE", it.message, it)
                    }
                }
                list.remove(card)
                refresh()
            })
        }

        private fun addKeyboardListener() {
            itemView.find.setOnClickListener { Toast.makeText(context, "Temporaly disabled", Toast.LENGTH_SHORT).show() }
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

        private fun tryToAdd(location: String, index: Int = list.lastIndex) {
            val card = ProxyCard(location)
            list.add(index, card)
            refresh()
        }

        private fun addDeleteListener(position: Int) {
            itemView.deleteItem.setOnClickListener {
                list.remove(list[position])
                refresh()
            }
        }

        private fun addCopyListener(position: Int) {
            itemView.copyLast.setOnClickListener {
                tryToAdd(list[position].url!!, position)
            }
        }
    }

    class NothingOnClipboard : Exception()
}
