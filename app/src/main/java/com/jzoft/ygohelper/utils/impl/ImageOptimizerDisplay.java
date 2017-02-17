package com.jzoft.ygohelper.utils.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jzoft.ygohelper.utils.ImageOptimizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by jjimenez on 13/10/16.
 */
public class ImageOptimizerDisplay implements ImageOptimizer {
    @Override
    public byte[] optimizeImage(byte[] image) {
        Bitmap bitmap = scaleDown(BitmapFactory.decodeStream(new ByteArrayInputStream(image)), 120, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    public static Bitmap scaleDown(Bitmap realImage, float widthExpected, boolean filter) {
        float ratio = (float) widthExpected / realImage.getWidth();
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
