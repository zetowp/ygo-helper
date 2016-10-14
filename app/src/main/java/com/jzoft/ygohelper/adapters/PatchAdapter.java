package com.jzoft.ygohelper.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jzoft.ygohelper.R;
import com.jzoft.ygohelper.biz.Patch;
import com.jzoft.ygohelper.biz.PatchHolder;
import com.jzoft.ygohelper.biz.PatchPrinter;
import com.jzoft.ygohelper.biz.impl.PatchLocatorLinked;
import com.jzoft.ygohelper.biz.impl.PatchLocatorUrlToImage;
import com.jzoft.ygohelper.biz.impl.PatchLocatorUrlWikiaToImageUrl;
import com.jzoft.ygohelper.biz.impl.PatchLocatorWordToUrlWikia;
import com.jzoft.ygohelper.biz.impl.PatchPrinterHtml;
import com.jzoft.ygohelper.databinding.CardSampleBinding;
import com.jzoft.ygohelper.utils.HttpCaller;
import com.jzoft.ygohelper.utils.HttpCallerFactory;
import com.jzoft.ygohelper.utils.ImageOptimizer;
import com.jzoft.ygohelper.utils.impl.ImageOptimizerDisplay;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjimenez on 13/10/16.
 */
public class PatchAdapter extends RecyclerView.Adapter<PatchAdapter.ViewHolder> {

    private final List<Patch> list;
    private final PatchHolder patchHolder;
    private ClipboardManager clipboard;
    private InputMethodManager keyboard;
    private Context context;
    private PatchPrinter printer;

    public PatchAdapter(ClipboardManager clipboard, InputMethodManager keyboard, Context context) {
        this.clipboard = clipboard;
        this.keyboard = keyboard;
        this.context = context;
        ImageOptimizer optimizer = new ImageOptimizerDisplay();
        HttpCallerFactory caller = new HttpCallerFactory();
        patchHolder = new PatchHolder(PatchLocatorLinked.buildPatchLocatorLinked(new PatchLocatorWordToUrlWikia(), new PatchLocatorUrlWikiaToImageUrl(caller), new PatchLocatorUrlToImage(caller, optimizer)));
        list = new ArrayList<Patch>();
        list.add(new Patch(null, null));
        printer = new PatchPrinterHtml(context);
    }

    public void print() {
        printer.print(patchHolder.getPatches());
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
        Patch patch = list.get(position);
        if (patch.getImage() == null) {
            holder.binding.copyLast.setVisibility(View.GONE);
            holder.binding.deleteItem.setVisibility(View.GONE);
            holder.binding.imageSample.setImageResource(R.drawable.not_found);
        } else {
            holder.binding.copyLast.setVisibility(View.VISIBLE);
            holder.binding.deleteItem.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(patch.getImage()));
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
                    if (clipboard.getPrimaryClip().getItemCount() > 0) {
                        ClipData.Item itemAt = clipboard.getPrimaryClip().getItemAt(0);
                        patchHolder.add(itemAt.coerceToText(context).toString());
                        refresh();
                    } else {
                        Toast.makeText(context, "Nothing in the clipboard", Toast.LENGTH_LONG).show();
                    }
                } catch (HttpCaller.NotFound notFound) {
                    Toast.makeText(context, "Not Found: " + notFound.getUrl(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addDeleteListener(ViewHolder holder, final int position) {
        holder.binding.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patchHolder.remove(position);
                refresh();
            }
        });
    }

    private void addCopyListener(ViewHolder holder, final int position) {
        holder.binding.copyLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patchHolder.copy(position);
                refresh();
            }
        });
    }

    private void refresh() {
        list.clear();
        list.addAll(patchHolder.getPatches());
        list.add(new Patch(null, null));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
