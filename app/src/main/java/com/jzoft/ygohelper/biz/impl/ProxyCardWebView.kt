package com.jzoft.ygohelper.biz.impl

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jzoft.ygohelper.R
import com.jzoft.ygohelper.biz.ProxyCard
import com.jzoft.ygohelper.biz.ProxyCardPrinter

/**
 * Created by jjimenez on 14/10/16.
 */
class ProxyCardWebView(private val context: Context) : ProxyCardPrinter {

    override fun print(proxyCards: List<ProxyCard>) {
        val html = buildHtml(proxyCards)
        doWebViewPrint(html)
    }


    private fun doWebViewPrint(htmlDocument: String) {
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                createWebPrintJob(view)
            }
        }
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)
    }


    private fun createWebPrintJob(webView: WebView) {
        (context.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->
            val jobName = "${context.getString(R.string.app_name)} Document"
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            val attributes = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                    .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
            printManager.print(jobName, printAdapter, attributes)
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
                "}" +
                "*{margin:0px; padding:0px}\n" +
                "body,html{padding:0px;margin:0px;}" +
                "<meta name=\" viewport \" content=\" initial -scale = 1, maximum-scale = 1, user-scalable = no, width = device-width\">\n" +
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
