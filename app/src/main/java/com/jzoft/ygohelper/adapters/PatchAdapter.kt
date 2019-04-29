package com.jzoft.ygohelper.adapters

import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.FragmentManager
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.appKodein

import com.jzoft.ygohelper.R
import com.jzoft.ygohelper.biz.*
import com.jzoft.ygohelper.dialogs.CardNameDialog
import com.jzoft.ygohelper.gone
import com.jzoft.ygohelper.loadUrl
import com.jzoft.ygohelper.utils.Caller
import com.jzoft.ygohelper.visible
import kotlinx.android.synthetic.main.card_sample.view.*

import java.io.ByteArrayInputStream
import java.io.File

/**
 * Created by jjimenez on 13/10/16.
 */
class PatchAdapter(private val clipboard: ClipboardManager, private val keyboard: InputMethodManager,
                   private val context: Context, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<PatchAdapter.ProxyViewHolder>() {

    val kodein = LazyKodein(context.appKodein)

    private val list: MutableList<ProxyCard>
    private val loader: ProxyCardLoader
    private val printer: ProxyCardPrinter
    private val localizator: ProxyCardLocator
    private val urlLocator: ProxyCardLocator

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
        urlLocator = kodein().instance("urlLocator")
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
        loader.saveAll(list.filter { it.save })
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
                if (proxyCard.image == null) download(proxyCard, proxyCard.url
                        ?: "NOT_FOUND", itemView.imageSample)
                else setImage(proxyCard, itemView.imageSample)
            }
            addCopyListener(index)
            addDeleteListener(index)
            addPasteListener()
            addKeyboardListener()
        }

        private fun setImage(proxyCard: ProxyCard, image: ImageView) {
            image.setImageBitmap(BitmapFactory.decodeStream(ByteArrayInputStream(proxyCard.image)))
        }

        private fun download(proxyCard: ProxyCard, location: String, image: ImageView) {
//            useGlideLocator(proxyCard, location, image)
            useHttpLocator(proxyCard, location, image)
        }

        private fun useGlideLocator(proxyCard: ProxyCard, location: String, image: ImageView) {
            urlLocator.locate(location).subscribe({
                image.loadUrl(it.url!!, R.drawable.downloading, onSuccess = {
                    proxyCard.url = it.url
                    proxyCard.save = true
                    saveList()
                }, onError = {
                    proxyCard.save = false
                    list.remove(proxyCard)
                    refresh()
                })
            }, errorOnLocator(location, proxyCard))
        }

        private fun useHttpLocator(proxyCard: ProxyCard, location: String, image: ImageView) {
            image.setImageResource(R.drawable.downloading)
            locateCard(proxyCard, location, image)
        }

        private fun locateCard(card: ProxyCard, location: String, image: ImageView) {
            localizator.locate(card.url!!).subscribe({
                card.url = it.url
                card.image = it.image
                setImage(card, image)
                card.save = true
                saveList()
            }, errorOnLocator(location, card))
        }

        private fun useLocator(card: ProxyCard, image: ImageView, location: String) {
            localizator.locate(card.url!!).subscribe({
                card.url = it.url
                card.image = it.image
                setImage(card, image)
                saveList()
                card.save = true
            }, errorOnLocator(location, card))
        }

        private fun errorOnLocator(location: String, card: ProxyCard): (Throwable) -> Unit {
            return {
                when (it) {
                    is Caller.NotFound -> Toast.makeText(context, "Not Found: " + it.url, Toast.LENGTH_LONG).show()
                    else -> {
                        Toast.makeText(context, "Entry error:  $location", Toast.LENGTH_LONG).show()
                        Log.e("ERROR_IMAGE", it.message, it)
                    }
                }
                card.save = false
                list.remove(card)
                refresh()
            }
        }

        private fun addKeyboardListener() {
            itemView.find.setOnClickListener {
                CardNameDialog.build { tryToAdd(it) }.show(fragmentManager, "show_search")
            }
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
