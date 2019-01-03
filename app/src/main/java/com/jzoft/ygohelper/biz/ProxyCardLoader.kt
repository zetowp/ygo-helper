package com.jzoft.ygohelper.biz

import com.jzoft.ygohelper.utils.HttpCaller

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.util.HashMap
import java.util.LinkedList

/**
 * Created by jjimenez on 16/02/17.
 */
class ProxyCardLoader(private val file: File, private val urlToImage: ProxyCardLocator) {

    fun loadAll(): List<ProxyCard> {
        val proxyCards = LinkedList<ProxyCard>()
        if (!file.exists()) {
            tryToCreate()
            return proxyCards
        }
        fileToPatches(proxyCards)
        return proxyCards
    }

    private fun tryToCreate() {
        try {
            file.createNewFile()
        } catch (e: IOException) {
            IllegalStateException(e)
        }

    }

    private fun fileToPatches(proxyCards: LinkedList<ProxyCard>) {
        val cache = HashMap<String, ByteArray>()
        try {
            val reader = BufferedReader(FileReader(file))
            var line: String? = reader.readLine()
            while (line != null) {
                proxyCards.add(toPatch(line, cache))
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun toPatch(line: String, cache: MutableMap<String, ByteArray>): ProxyCard {
        val bytes = cache[line]
        if (bytes == null) {
            try {
                val proxy = urlToImage.locate(line)
                cache[proxy.url!!] = proxy.image!!
                return proxy
            } catch (notFound: HttpCaller.NotFound) {
                IllegalStateException()
            }

        }
        return ProxyCard(line, bytes)
    }

    fun saveAll(proxyCards: List<ProxyCard>) {
        clear(file)
        try {
            val writer = FileWriter(file)
            for (p in proxyCards) {
                writer.append(p.url)
                writer.append('\n')
            }
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

    }

    fun clear(file: File) {
        try {
            val writer = PrintWriter(file)
            writer.print("")
            writer.flush()
            writer.close()
        } catch (e: FileNotFoundException) {
            tryToCreate()
        }

    }
}
