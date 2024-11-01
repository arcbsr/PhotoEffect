//package com.crop.phototocartooneffect.renderengins.effects;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.renderscript.Allocation;
//import android.renderscript.Element;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicConvolve3x3;
//
//import androidx.annotation.NonNull;
//
//import com.crop.phototocartooneffect.renderengins.ImageEffect;
//
//public class CartoonEffect implements ImageEffect {
//    private final RenderScript rs;
//
//    public CartoonEffect(RenderScript rs) {
//        this.rs = rs;
//    }
//
//    @Override
//    public void applyEffect(@NonNull Bitmap bitmap, @NonNull ImageEffect.ImageEffectCallback callback) {
//        try {
//            Bitmap edgesBitmap = detectEdges(bitmap);
//            Bitmap quantizedBitmap = quantizeColors(bitmap, 8); // Quantize to 8 colors
//            Bitmap cartoonBitmap = combineEffects(quantizedBitmap, edgesBitmap);
//
//            // Return the resulting bitmap via callback
//            callback.onSuccess(cartoonBitmap);
//        } catch (Exception e) {
//            callback.onError(e);
//        }
//    }
//
//    @Override
//    public void applyEffectWithData(@NonNull Bitmap bitmap, @NonNull ImageEffectCallback callback) {
//
//    }
//
//    // Detects edges in the image using a Sobel filter
//    private Bitmap detectEdges(Bitmap input) {
//        Bitmap edgeBitmap = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
//
//        Allocation inAlloc = Allocation.createFromBitmap(rs, input);
//        Allocation outAlloc = Allocation.createFromBitmap(rs, edgeBitmap);
//
//        ScriptIntrinsicConvolve3x3 convolve = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
//
//        // Sobel filter for edge detection
//        float[] sobelX = {
//                -1, 0, 1,
//                -2, 0, 2,
//                -1, 0, 1
//        };
//        convolve.setCoefficients(sobelX);
//        convolve.setInput(inAlloc);
//        convolve.forEach(outAlloc);
//
//        outAlloc.copyTo(edgeBitmap);
//
//        return edgeBitmap;
//    }
//
//    // Applies color quantization by reducing the color palette
//    private Bitmap quantizeColors(Bitmap input, int colorCount) {
//        Bitmap quantizedBitmap = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
//
//        for (int y = 0; y < input.getHeight(); y++) {
//            for (int x = 0; x < input.getWidth(); x++) {
//                int pixel = input.getPixel(x, y);
//
//                int red = (Color.red(pixel) / colorCount) * colorCount;
//                int green = (Color.green(pixel) / colorCount) * colorCount;
//                int blue = (Color.blue(pixel) / colorCount) * colorCount;
//
//                int newColor = Color.rgb(red, green, blue);
//                quantizedBitmap.setPixel(x, y, newColor);
//            }
//        }
//
//        return quantizedBitmap;
//    }
//
//    // Combines the quantized image and edge detection output for cartoon effect
//    private Bitmap combineEffects(Bitmap quantizedBitmap, Bitmap edgesBitmap) {
//        Bitmap outputBitmap = Bitmap.createBitmap(quantizedBitmap.getWidth(), quantizedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
//        for (int y = 0; y < quantizedBitmap.getHeight(); y++) {
//            for (int x = 0; x < quantizedBitmap.getWidth(); x++) {
//                int quantizedColor = quantizedBitmap.getPixel(x, y);
//                int edgeColor = edgesBitmap.getPixel(x, y);
//
//                // If edge detected, use black color; otherwise, use the quantized color
//                int outputColor = Color.red(edgeColor) < 128 ? Color.BLACK : quantizedColor;
//                outputBitmap.setPixel(x, y, outputColor);
//            }
//        }
//
//        return outputBitmap;
//    }
//}
