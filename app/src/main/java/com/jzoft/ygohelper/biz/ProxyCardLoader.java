package com.jzoft.ygohelper.biz;

import com.jzoft.ygohelper.utils.HttpCaller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jjimenez on 16/02/17.
 */
public class ProxyCardLoader {

    private File file;
    private ProxyCardLocator urlToImage;


    public ProxyCardLoader(File file, ProxyCardLocator urlToImage) {
        this.file = file;
        this.urlToImage = urlToImage;
    }

    public List<ProxyCard> loadAll() {
        LinkedList<ProxyCard> proxyCards = new LinkedList<>();
        if (!file.exists()) {
            tryToCreate();
            return proxyCards;
        }
        fileToPatches(proxyCards);
        return proxyCards;
    }

    private void tryToCreate() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            new IllegalStateException(e);
        }
    }

    private void fileToPatches(LinkedList<ProxyCard> proxyCards) {
        Map<String, byte[]> cache = new HashMap<String, byte[]>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                proxyCards.add(toPatch(line, cache));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProxyCard toPatch(String line, Map<String, byte[]> cache) {
        byte[] bytes = cache.get(line);
        if (bytes == null) {
            try {
                ProxyCard proxy = urlToImage.locate(line);
                cache.put(proxy.getUrl(), proxy.getImage());
                return proxy;
            } catch (HttpCaller.NotFound notFound) {
                new IllegalStateException();
            }
        }
        return new ProxyCard(line, bytes);
    }

    public void saveAll(List<ProxyCard> proxyCards) {
        clear(file);
        try {
            FileWriter writer = new FileWriter(file);
            for (ProxyCard p : proxyCards) {
                writer.append(p.getUrl());
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void clear(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            tryToCreate();
        }
    }
}
