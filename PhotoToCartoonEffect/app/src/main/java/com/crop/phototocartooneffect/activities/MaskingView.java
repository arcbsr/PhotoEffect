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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalBitmap != null) {
            // Calculate the center position
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            int bitmapWidth = originalBitmap.getWidth();
            int bitmapHeight = originalBitmap.getHeight();

            // Calculate x and y position to center the bitmap
            float x = (canvasWidth - bitmapWidth) / 2f;
            float y = (canvasHeight - bitmapHeight) / 2f;

            // Draw the original bitmap in the center
//            canvas.drawBitmap(originalBitmap, x, y, null);
        }

        // Draw the mask bitmap
        if (maskBitmap != null) {
            canvas.drawBitmap(maskBitmap, 0, 0, null);
        }

        // Draw the path on the canvas (temporary visual feedback)
        if (!isErasing) {
            canvas.drawPath(path, paint);
        }
    }

    public void clear() {
        saveMaskState();
        Canvas canvas = new Canvas(maskBitmap);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // Use clear mode for erasing
        canvas.drawPath(path, paint); // Draw the path onto the mask bitmap
        paint.setXfermode(null); // Reset Xfermode to allow normal drawing again
        paint.setColor(Color.argb(128, 0, 255, 0)); // Reset color back to green or desired color
        path.reset(); // Clear the path for the next drawing
        invalidate(); // Redraw to show the updated mask
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate(); // Redraw to show the current path
                break;
            case MotionEvent.ACTION_UP:
//                saveMaskState();
//                Canvas canvas = new Canvas(maskBitmap);
//                if (isErasing) {
//                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // Use clear mode for erasing
//                } else {
//                    paint.setXfermode(null); // Reset Xfermode for drawing
//                }
//                canvas.drawPath(path, paint); // Draw the path onto the mask bitmap
//                path.reset(); // Clear the path for the next drawing
//                invalidate(); // Redraw to show the updated mask
                break;
        }
        return true;
    }

    public void setOriginalBitmap(Bitmap bitmap) {
        this.originalBitmap = bitmap;
        invalidate();
    }

    public void setMaskBitmap(Bitmap mask) {
        this.maskBitmap = mask;
        invalidate();
    }

    private void saveMaskState() {
        if (maskBitmap != null) {
            maskStack.push(maskBitmap.copy(maskBitmap.getConfig(), true));
        }
    }

    public void undoMask() {
        if (!maskStack.isEmpty()) {
            maskBitmap = maskStack.pop();
            invalidate();
        }
    }

    public void setMaskBitmapSize(int width, int height) {
        maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        maskBitmap.eraseColor(Color.TRANSPARENT);
    }

    public void setErasingMode(boolean erasing) {
        isErasing = erasing;
    }
}
