package com.crop.phototocartooneffect.renderengins.effects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import java.nio.ByteBuffer;

public class BackgroundRemoveFML implements ImageEffect {
    private Segmenter segmenter;

    public BackgroundRemoveFML() {
        SelfieSegmenterOptions options = new SelfieSegmenterOptions.Builder()
                .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
                .enableRawSizeMask()
                .build();
        segmenter = Segmentation.getClient(options);
    }

    @Override
    public void applyEffect(@NonNull Bitmap bitmap, @NonNull ImageEffectCallback callback) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        Segmentation.getClient(new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                        .build())
                .process(inputImage)
                .addOnSuccessListener(segmentationResult -> {
                    Bitmap segmentedBitmap = createSegmentedBitmap(bitmap, segmentationResult);
                    callback.onSuccess(segmentedBitmap,"");
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void applyEffectWithData(@NonNull ImageEffectCallback callback, Context context) {
        // Implement the logic to apply the effect with additional data if needed

    }

    private Bitmap createSegmentedBitmap(Bitmap originalBitmap, SegmentationMask segmentationResult) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        ByteBuffer mask = segmentationResult.getBuffer();
        mask.rewind();

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
        return outputBitmap;
    }
}

