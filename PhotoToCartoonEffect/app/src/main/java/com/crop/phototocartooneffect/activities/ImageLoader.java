package com.crop.phototocartooneffect.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;

import retrofit2.http.Url;

public class ImageLoader {

    private static ImageLoader instance;
    private BitmapCache bitmapCache;


    private ImageLoader() {
        bitmapCache = new BitmapCache();
    }

    public static synchronized ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public Bitmap getBitmap(String key, OnImageLoadedListener listener) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key, -1);
            return cachedBitmap;
        }
        return null;

    }

    public Bitmap getBitmap(String key) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key);

        if (cachedBitmap != null) {
            return cachedBitmap;
        }
        return null;

    }

    interface OnImageLoadedListener {
        void onImageLoaded(Bitmap bitmap, String keyValue, int position);
    }

    public void loadBitmap(String key, Bitmap bitmap) {
        bitmapCache.addBitmapToCache(key, bitmap);
    }

    public void loadBitmap(Context context, String url, String key2, OnImageLoadedListener listener) {
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key2);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key2, 0);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            bitmapCache.addBitmapToCache(key2, resource);
                            listener.onImageLoaded(resource, key2, 0);
                        }
                    });
        }
    }

    public void loadBitmap(Context context, Uri uri, int position, OnImageLoadedListener listener) {
        String key2 = uri.toString();
        Bitmap cachedBitmap = bitmapCache.getBitmapFromCache(key2);

        if (cachedBitmap != null) {
            listener.onImageLoaded(cachedBitmap, key2, position);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .into(new com.bumptech.glide.request.target.SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            bitmapCache.addBitmapToCache(key2, resource);
                            listener.onImageLoaded(resource, key2, position);
                        }
                    });
        }

        if (true) {
            return;
        }
        String key = uri.toString();
        Bitmap bitmap = bitmapCache.getBitmapFromCache(key);

        if (bitmap == null) {
            try {
                // Load the Bitmap from the URI
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));

                // Add the Bitmap to the cache
                if (bitmap != null) {
                    bitmapCache.addBitmapToCache(key, bitmap);
                }
            } catch (Exception e) {
                Log.e("ImageLoader", "Error loading image", e);
            }
        }

//        return bitmap;
    }
}
