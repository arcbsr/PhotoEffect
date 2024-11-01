package com.crop.phototocartooneffect.animationmodels;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.crop.phototocartooneffect.activities.VideoFrames;
import com.crop.phototocartooneffect.activities.VideoRenderEngin;

public class ScaleFrameDrawingStrategy implements FrameDrawingStrategy {

    @Override
    public void drawFrame(Bitmap bitmap, int screenWidth, int screenHeight, VideoFrames.FrameRate frameRate) {
        float angle = 0f; // Start rotation from 0 degrees
        float rotationSpeed = 90.0f; // Degrees per second

        // Calculate the aspect ratio of the bitmap and the output video
        float bitmapAspectRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        float videoAspectRatio = (float) screenWidth / (float) screenHeight;
        int counter = 0;
        int drawWidth, drawHeight;
        if (bitmapAspectRatio > videoAspectRatio) {
            drawWidth = screenWidth;
            drawHeight = (int) (screenWidth / bitmapAspectRatio);
        } else {
            drawHeight = screenHeight;
            drawWidth = (int) (screenHeight * bitmapAspectRatio);
        }
        long previousTime = System.currentTimeMillis();
        do {

            // Calculate the position to center the bitmap
            int left = (screenWidth - drawWidth) / 2;
            int top = (screenHeight - drawHeight) / 2;

            // Create a matrix for rotation
            Matrix matrix = new Matrix();
            matrix.postTranslate(left, top);

            float centerX = left + drawWidth / 2.0f;
            float centerY = top + drawHeight / 2.0f;

            // Rotate and scale the matrix based on angle and center
            matrix.postRotate(angle, centerX, centerY);
            float scaleFactor = 1.0f - ((360f - angle) / 360.0f);
            matrix.postScale(scaleFactor * (float) drawWidth / bitmap.getWidth(), scaleFactor * (float) drawHeight / bitmap.getHeight(), centerX, centerY);
            VideoRenderEngin.getInstance().createCanvasForRender(bitmap, matrix, new Paint());

            // Update the angle based on elapsed time
            long currentTime = System.currentTimeMillis();
            float elapsedTimeSec = (currentTime - previousTime) / 1000.0f;
            previousTime = currentTime;

            angle += rotationSpeed * elapsedTimeSec; // Increment angle based on rotation speed and time
            //if (angle >= 360) angle -= 360; // Reset angle after a full rotation

            Log.e("Rafiur", "angle: " + angle);
            Log.e("Rafiur", "counter: " + counter);
            counter++;
        } while (angle < 360);
    }
}
