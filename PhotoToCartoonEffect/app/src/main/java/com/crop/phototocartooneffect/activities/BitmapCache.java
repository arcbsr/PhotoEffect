package com.crop.phototocartooneffect.activities;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCache {
    private LruCache<String, Bitmap> memoryCache;

    public BitmapCache() {
        // Initialize the cache size (e.g., 1/8th of available memory)
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // Calculate the size of the bitmap in kilobytes
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    // Method to add Bitmap to cache
    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    // Method to get Bitmap from cache
    public Bitmap getBitmapFromCache(String key) {
        return memoryCache.get(key);
    }

    // Optional: Method to clear the cache
    public void clearCache() {
        memoryCache.evictAll();
    }
}
