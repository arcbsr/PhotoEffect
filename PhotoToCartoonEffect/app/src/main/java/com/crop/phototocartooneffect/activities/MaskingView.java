package com.crop.phototocartooneffect.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Stack;

public class MaskingView extends View {
    private Bitmap originalBitmap;
    private Bitmap maskBitmap;
    private Paint paint;
    private Path path;
    private Stack<Bitmap> maskStack = new Stack<>();
    private boolean isErasing = false;
    private float offsetX = 0f;
    private float offsetY = 0f;
    private float scaleFactor = 1.0f;

    private static final float SCALE_STEP = 0.1f;
    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_SCALE = 2.0f;

    public MaskingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.argb(128, 0, 255, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20); // Set stroke width
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        path = new Path();
        maskStack.push(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)); // Initial empty state
    }

    public void scaleIn() {
        if (scaleFactor < MAX_SCALE) {
            scaleFactor += SCALE_STEP;
            updateMaskBitmap();
        }
    }

    public void scaleOut() {
        if (scaleFactor > MIN_SCALE) {
            scaleFactor -= SCALE_STEP;
            updateMaskBitmap();
        }
    }

    public Bitmap scaleMaskBitmapToScreenSize(Bitmap bitmap) {
        if (bitmap != null) {
            int screenWidth = getWidth();
            int screenHeight = getHeight();

            float widthRatio = (float) screenWidth / bitmap.getWidth();
            float heightRatio = (float) screenHeight / bitmap.getHeight();

            scaleFactor = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (bitmap.getWidth() * scaleFactor);
            int newHeight = (int) (bitmap.getHeight() * scaleFactor);
            offsetX = (getWidth() - newWidth) / 2f;
            offsetY = (getHeight() - newHeight) / 2f;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            return scaledBitmap;
        }
        return null;
    }

        private void updateMaskBitmap () {
            if (maskBitmap != null) {
                invalidate();
            }
        }

        @Override
        protected void onDraw (Canvas canvas){
            super.onDraw(canvas);

            // Apply scaling and translation to the canvas for consistent transformations
            canvas.save();
            canvas.translate(offsetX, offsetY);
            canvas.scale(scaleFactor, scaleFactor);

            // Draw the original bitmap
            if (originalBitmap != null) {
                float x = (getWidth() - originalBitmap.getWidth() * scaleFactor) / 2f / scaleFactor;
                float y = (getHeight() - originalBitmap.getHeight() * scaleFactor) / 2f / scaleFactor;
                canvas.drawBitmap(originalBitmap, x, y, null);
            }

            // Draw the mask bitmap
            if (maskBitmap != null) {

                float x = (getWidth() - maskBitmap.getWidth() * scaleFactor) / 2f / scaleFactor;
                float y = (getHeight() - maskBitmap.getHeight() * scaleFactor) / 2f / scaleFactor;
//                canvas.drawBitmap(maskBitmap, x, y, null);
//                canvas.drawBitmap(maskBitmap, 0, 0, null); // (0,0) since canvas is already translated
            }

            // Draw the path
            if (!isErasing) {
                canvas.save();
//                canvas.translate(offsetX / scaleFactor, offsetY / scaleFactor); // Apply inverse translation
                canvas.drawPath(path, paint);
                canvas.restore();
            }

            canvas.restore();
        }

        @Override
        public boolean onTouchEvent (MotionEvent event){
            // Adjust touch coordinates according to scale and offset
            float x = (event.getX() - offsetX) / scaleFactor;
            float y = (event.getY() - offsetY) / scaleFactor;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x, y);
                    invalidate();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:

                    break;
            }
            return true;
        }

        public void setOriginalBitmap (Bitmap bitmap){
            this.originalBitmap = scaleMaskBitmapToScreenSize(bitmap);
            invalidate();
        }

        public void setMaskBitmap (Bitmap mask){
            this.maskBitmap = scaleMaskBitmapToScreenSize(mask);
            invalidate();
        }

        private void saveMaskState () {
            if (maskBitmap != null) {
                maskStack.push(maskBitmap.copy(maskBitmap.getConfig(), true));
            }
        }

        public void undoMask () {
            if (!maskStack.isEmpty()) {
                maskBitmap = maskStack.pop();
                invalidate();
            }
        }

        public void setMaskBitmapSize ( int width, int height){
            maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            maskBitmap.eraseColor(Color.TRANSPARENT);
        }

        public void clear () {
            setErasingMode(true);
            // Save the drawn path to the mask bitmap
            saveMaskState(); // Push the current state to maskStack for undo
            Canvas canvas = new Canvas(originalBitmap);
            canvas.drawPath(path, paint);
            path.reset();
            invalidate();
            setErasingMode(false);
        }

        public void setErasingMode ( boolean erasing){
            isErasing = erasing;
            if (isErasing) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // Enable erase mode
            } else {
                paint.setXfermode(null); // Disable erase mode
                paint.setColor(Color.argb(128, 0, 255, 0)); // Set color back to green
            }
        }
    }
