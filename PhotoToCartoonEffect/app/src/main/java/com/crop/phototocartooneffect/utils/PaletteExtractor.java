package com.crop.phototocartooneffect.utils;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.crop.phototocartooneffect.R;

public class PaletteExtractor {

    public void setFallbackColorResId(int fallbackColorResId) {
        this.fallbackColorResId = fallbackColorResId;
    }

    private int fallbackColorResId = R.color.card_prnt;
    private Palette palette;

    public PaletteExtractor(Bitmap bitmap, final OnPaletteGeneratedListener listener) {
        // Generate the palette asynchronously and notify listener
        Palette.from(bitmap).generate(p -> {
            palette = p;
            if (listener != null) {
                listener.onPaletteGenerated(PaletteExtractor.this);
            }
        });
    }

    // Interface to notify when palette is generated
    public interface OnPaletteGeneratedListener {
        void onPaletteGenerated(PaletteExtractor paletteExtractor);
    }

    // Method to get a specific color with a fallback
    public int getVibrantColor(Context context) {
        return palette != null ? palette.getVibrantColor(ContextCompat.getColor(context, fallbackColorResId)) : ContextCompat.getColor(context, fallbackColorResId);
    }

    public int getMutedColor(Context context) {
        return palette != null ? palette.getMutedColor(ContextCompat.getColor(context, fallbackColorResId)) : ContextCompat.getColor(context, fallbackColorResId);
    }

    public int getDarkVibrantColor(Context context) {
        return palette != null ? palette.getDarkVibrantColor(ContextCompat.getColor(context, fallbackColorResId)) : ContextCompat.getColor(context, fallbackColorResId);
    }

    public int getDarkMutedColor(Context context) {
        return palette != null ? palette.getDarkMutedColor(ContextCompat.getColor(context, fallbackColorResId)) : ContextCompat.getColor(context, fallbackColorResId);
    }

    public int getLightVibrantColor(Context context) {
        return palette != null ? palette.getLightVibrantColor(ContextCompat.getColor(context, fallbackColorResId)) : ContextCompat.getColor(context, fallbackColorResId);
    }

    public int getLightMutedColor(Context context) {
        return palette != null ? palette.getLightMutedColor(ContextCompat.getColor(context, fallbackColorResId)) : ContextCompat.getColor(context, fallbackColorResId);
    }

    // Example to retrieve a Swatch if you need more information (like text colors)
    public Palette.Swatch getVibrantSwatch() {
        return palette != null ? palette.getVibrantSwatch() : null;
    }
    public int getBackgroundColor(Context context) {
        return getLightVibrantColor(context);
    }
}
