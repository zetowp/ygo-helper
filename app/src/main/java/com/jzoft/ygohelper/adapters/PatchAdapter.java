package com.jzoft.ygohelper.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jzoft.ygohelper.R;
import com.jzoft.ygohelper.biz.ProxyCard;
import com.jzoft.ygohelper.biz.ProxyCardHolder;
import com.jzoft.ygohelper.biz.ProxyCardLoader;
import com.jzoft.ygohelper.biz.ProxyCardLocator;
import com.jzoft.ygohelper.biz.ProxyCardPrinter;
import com.jzoft.ygohelper.biz.impl.ProxyCardLocatorLinked;
import com.jzoft.ygohelper.biz.impl.ProxyCardLocatorUrlToImage;
import com.jzoft.ygohelper.biz.impl.ProxyCardLocatorUrlWikiaToImageUrl;
import com.jzoft.ygohelper.biz.impl.ProxyCardLocatorWordToUrlWikia;
import com.jzoft.ygohelper.biz.impl.ProxyCardPrinterHtml;
import com.jzoft.ygohelper.databinding.CardSampleBinding;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;
import com.jzoft.ygohelper.utils.impl.ImageOptimizerDisplay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jjimenez on 13/10/16.
 */
public class PatchAdapter extends RecyclerView.Adapter<PatchAdapter.ViewHolder> {

    private final List<ProxyCard> list;
    private final ProxyCardHolder proxyCardHolder;
    private final ProxyCardLoader loader;
    private ClipboardManager clipboard;
    private InputMethodManager keyboard;
    private Context context;
    private ProxyCardPrinter printer;

    public PatchAdapter(ClipboardManager clipboard, InputMethodManager keyboard, Context context) {
        this.clipboard = clipboard;
        this.keyboard = keyboard;
        this.context = context;
        HttpCallerFactory caller = new HttpCallerFactory();
        ProxyCardLocator urlToImage = new ProxyCardLocatorUrlToImage(caller, new ImageOptimizerDisplay());
        proxyCardHolder = new ProxyCardHolder(ProxyCardLocatorLinked.buildPatchLocatorLinked(
                new ProxyCardLocatorWordToUrlWikia(), new ProxyCardLocatorUrlWikiaToImageUrl(caller),
                urlToImage));
        loader = new ProxyCardLoader(getProxyFile(), urlToImage);
        printer = new ProxyCardPrinterHtml(context);
        list = new LinkedList<ProxyCard>();
        proxyCardHolder.getProxyCards().addAll(loader.loadAll());
        refresh();
    }

    @NonNull
    private File getProxyFile() {
        File root = buildLoadFile();
        if (!root.exists())
            root.mkdir();
        return new File(root, "proxy.txt");
    }

    private File buildLoadFile() {
        return new File(context.getFilesDir(), "proxys");
    }

    public void print() {
        printer.print(proxyCardHolder.getProxyCards());
    }

    public void clear() {
        proxyCardHolder.getProxyCards().clear();
        refresh();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardSampleBinding binding;

        public ViewHolder(CardSampleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardSampleBinding cardInfator = DataBindingUtil.inflate(inflater, R.layout.card_sample, parent, false);
        return new ViewHolder(cardInfator);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProxyCard proxyCard = list.get(position);
        if (proxyCard.getImage() == null) {
            holder.binding.copyLast.setVisibility(View.GONE);
            holder.binding.deleteItem.setVisibility(View.GONE);
            holder.binding.imageSample.setImageResource(R.drawable.not_found);
        } else {
            holder.binding.copyLast.setVisibility(View.VISIBLE);
            holder.binding.deleteItem.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(proxyCard.getImage()));
            holder.binding.imageSample.setImageBitmap(bitmap);
        }
        addCopyListener(holder, position);
        addDeleteListener(holder, position);
        addPasteListener(holder);
        addKeyboardListener(holder);
    }

    private void addKeyboardListener(ViewHolder holder) {
        holder.binding.find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Temporaly disabled", Toast.LENGTH_SHORT);
            }
        });
    }


    private String getTextFromVirtualKeyboard() {
        keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        return "Hola mundo";
    }

    private void addPasteListener(ViewHolder holder) {
        holder.binding.pasteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tryToAdd(getLocation());
                } catch (NothingOnClipboard e) {
                    Toast.makeText(context, "Nothing in the clipboard", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getLocation() throws NothingOnClipboard {
        if (clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemCount() > 0) {
            ClipData.Item itemAt = clipboard.getPrimaryClip().getItemAt(0);
            return itemAt.coerceToText(context).toString();
        } else {
            throw new NothingOnClipboard();
        }
    }

    private void tryToAdd(String location) {
        try {
            proxyCardHolder.add(location);
            refresh();
        } catch (HttpCaller.NotFound notFound) {
            Toast.makeText(context, "Not Found: " + notFound.getUrl(), Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Entry error:  " + location, Toast.LENGTH_LONG).show();
        }
    }

    private void addDeleteListener(ViewHolder holder, final int position) {
        holder.binding.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proxyCardHolder.remove(position);
                refresh();
            }
        });
    }

    private void addCopyListener(ViewHolder holder, final int position) {
        holder.binding.copyLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proxyCardHolder.copy(position);
                refresh();
            }
        });
    }

    private void refresh() {
        list.clear();
        list.addAll(proxyCardHolder.getProxyCards());
        loader.saveAll(proxyCardHolder.getProxyCards());
        list.add(new ProxyCard(null, null));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static final class NothingOnClipboard extends Exception {

    }
}
