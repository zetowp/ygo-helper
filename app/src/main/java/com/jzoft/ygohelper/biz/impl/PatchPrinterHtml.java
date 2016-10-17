package com.jzoft.ygohelper.biz.impl;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.jzoft.ygohelper.biz.Patch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by jjimenez on 14/10/16.
 */
public class PatchPrinterHtml implements com.jzoft.ygohelper.biz.PatchPrinter {
    private static final String HTML_START = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
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
            "<table>";

    private static final String HTML_END = "</table>\n" +
            "</body>\n" +
            "</html>";
    private static final String HTML_TEMPLATE = "<td><img src=\"patch\" width=\"223px\" style=\"border-style:none;\"></td>";
    private Context context;

    public PatchPrinterHtml(Context context) {
        this.context = context;
    }

    @Override
    public void print(List<Patch> patches) {
        String html = buildHtml(patches);
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File toOpen = new File(downloads, "patches.html");
        createFile(html, toOpen);
        lunchBrowser(toOpen);
    }

    private void createFile(String html, File toOpen) {
        toOpen.delete();
        try {
            toOpen.setReadable(true);
            toOpen.setWritable(true);
            toOpen.createNewFile();
            FileWriter w = new FileWriter(toOpen);
            w.write(html);
            w.flush();
            w.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @NonNull
    private String buildHtml(List<Patch> patches) {
        StringBuilder html = new StringBuilder(HTML_START);
        if (!patches.isEmpty()) {
            buildTable(patches, html);
        }
        html.append(HTML_END);
        return html.toString();
    }

    private void buildTable(List<Patch> patches, StringBuilder html) {
        html.append("<tr>");
        for (int i = 0; i < patches.size(); i++) {
            html.append(HTML_TEMPLATE.replace("patch", patches.get(i).getUrl()));
            if (i % 3 == 2) html.append("</tr>\n<tr>");
        }
        html.append("</tr>");
    }

    public void lunchBrowser(File file) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension("html");
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }


}
