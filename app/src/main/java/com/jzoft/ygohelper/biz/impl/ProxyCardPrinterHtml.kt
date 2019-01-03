package com.jzoft.ygohelper.biz.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import android.widget.Toast

import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardPrinter

import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Created by jjimenez on 14/10/16.
 */
class ProxyCardPrinterHtml(private val context: Context) : ProxyCardPrinter {

    override fun print(proxyCards: List<ProxyCard>) {
        val html = buildHtml(proxyCards)
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val toOpen = File(downloads, "proxyCards.html")
        createFile(html, toOpen)
        sendEmail(toOpen)
    }


    private fun createFile(html: String, toOpen: File) {
        toOpen.delete()
        try {
            toOpen.setReadable(true)
            toOpen.setWritable(true)
            toOpen.createNewFile()
            val w = FileWriter(toOpen)
            w.write(html)
            w.flush()
            w.close()
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

    }

    private fun buildHtml(proxyCards: List<ProxyCard>): String {
        val html = StringBuilder(HTML_START)
        if (!proxyCards.isEmpty()) {
            buildTable(proxyCards, html)
        }
        html.append(HTML_END)
        return html.toString()
    }

    private fun buildTable(proxyCards: List<ProxyCard>, html: StringBuilder) {
        html.append("<tr>")
        for (i in proxyCards.indices) {
            html.append(HTML_TEMPLATE.replace("patch", proxyCards[i].url!!))
            if (i % 3 == 2) html.append("</tr>\n<tr>")
        }
        html.append("</tr>")
    }

    private fun lunchBrowser(file: File) {
        val myMime = MimeTypeMap.getSingleton()
        val newIntent = Intent(Intent.ACTION_VIEW)
        val mimeType = myMime.getMimeTypeFromExtension("html")
        newIntent.setDataAndType(Uri.fromFile(file), mimeType)
        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(newIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show()
        }

    }


    private fun sendEmail(toOpen: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "proxys")
        intent.putExtra(Intent.EXTRA_TEXT, "open attachment in browser and print page")
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(toOpen))
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "com.jzoft.ygohelper.provider", toOpen))
        context.startActivity(Intent.createChooser(intent, "Send email..."))
    }

    companion object {
        private val HTML_START = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "img {\n" +
                "    padding: 0;\n" +
                "    display:block;\n" +
                "    margin:0 auto 0px auto;\n" +
                "    border:none;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table>"

        private val HTML_END = "</table>\n" +
                "</body>\n" +
                "</html>"
        private val HTML_TEMPLATE = "<td><img src=\"patch\" width=\"223px\" style=\"border-style:none;\"></td>"
    }


}
