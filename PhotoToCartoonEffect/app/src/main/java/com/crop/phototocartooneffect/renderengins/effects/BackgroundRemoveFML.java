package com.crop.phototocartooneffect.renderengins.effects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import java.nio.ByteBuffer;

public class BackgroundRemoveFML implements ImageEffect {
    private static final String IMAGE_TAG = "BackgroundRemoveFML";
    private Segmenter segmenter;

    public BackgroundRemoveFML() {
        SelfieSegmenterOptions options = new SelfieSegmenterOptions.Builder().setDetectorMode(SelfieSegmenterOptions.STREAM_MODE).enableRawSizeMask().build();
        segmenter = Segmentation.getClient(options);
    }

    @Override
    public void applyEffect(@NonNull Bitmap bitmap, @NonNull ImageEffectCallback callback) {
        callback.onStartProcess();
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        Segmentation.getClient(new SelfieSegmenterOptions.Builder().setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE).build()).process(inputImage).addOnSuccessListener(new OnSuccessListener<SegmentationMask>() {
            @Override
            public void onSuccess(SegmentationMask segmentationResult) {
                BackgroundRemoveFML.this.createSegmentedBitmap(bitmap, segmentationResult, callback);
//                        ImageLoader.getInstance().loadBitmap(System.currentTimeMillis() + "", segmentedBitmap);
//                        callback.onSuccess(segmentedBitmap, System.currentTimeMillis() + "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void applyEffectWithData(@NonNull ImageEffectCallback callback, Context context) {
        // Implement the logic to apply the effect with additional data if needed

    }

    @Override
    public Boolean isBitmapHolder() {
        return true;
    }

    private void createSegmentedBitmap(final Bitmap originalBitmap, final SegmentationMask segmentationResult, ImageEffectCallback callback) {
        final int width = originalBitmap.getWidth();
        final int height = originalBitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final ByteBuffer mask = segmentationResult.getBuffer();
        mask.rewind();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        float maskValue = mask.getFloat();
                        if (maskValue > 0.5f) {
                            outputBitmap.setPixel(x, y, originalBitmap.getPixel(x, y));
                        } else {
                            outputBitmap.setPixel(x, y, Color.TRANSPARENT);
                        }
                    }
                }
                ImageLoader.getInstance().loadBitmap(System.currentTimeMillis() + "", outputBitmap);
                callback.onSuccess(outputBitmap, System.currentTimeMillis() + "");
            }
        }).start();
    }
}

