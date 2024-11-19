package com.crop.phototocartooneffect.popeffect.color_splash_tool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.BuildConfig;
import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.fragments.ImageAiFragment;
import com.crop.phototocartooneffect.popeffect.support.Constants;
import com.crop.phototocartooneffect.popeffect.support.ImageUtils;
import com.crop.phototocartooneffect.popeffect.support.StoreManager;
import com.crop.phototocartooneffect.utils.RLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;


public class ColorSplashActivity extends AppCompatActivity implements OnClickListener, OnSeekBarChangeListener {

    private int radiusbarPlus = 20;
    public static BrushView brushView;
    public static Bitmap colorBitmap;
    public static int displayHight;
    public static int displayWidth;
    public static Bitmap grayBitmap;
    public static SeekBar offsetBar;
    public static SeekBar opacityBar;
    public static ImageView prView;
    public static SeekBar radiusBar;
    public static String tempDrawPath;
    public static File tempDrawPathFile;
    public static TouchImageView tiv;
    public static Vector vector;
    RecyclerView recycler_view_recolr;

    public String mSelectedImagePath;
    public String mSelectedOutputPath;
    private RelativeLayout imageViewContainer;
    private ImageView colorBtn, grayBtn, offsetDemo, recolorBtn, zoomBtn;
    private String imageSavePath;
    private Bitmap hand;
    private LinearLayout offsetLayout, bottomBar;
    private Runnable runnableCode;
    private ProgressDialog saveLoader;

    public static Bitmap MASK_BITMAP;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(com.crop.phototocartooneffect.R.layout.layout_color_pop);
        if (colorBitmap == null) {
            Toast.makeText(this, "Error Bitmap", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        findViewById(R.id.closeIcon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.locking).setVisibility(View.GONE);
        this.imageSavePath = StoreManager.getstorageRootPath(this) + "/vitemp";
        this.tempDrawPath = StoreManager.getstorageRootPath(this) + "/vitemp";
        imageViewContainer = findViewById(R.id.imageViewContainer);
        this.saveLoader = new ProgressDialog(this);
        vector = new Vector();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        displayWidth = point.x;
        displayHight = point.y;
        this.hand = BitmapFactory.decodeResource(getResources(), R.drawable.hand);
        this.hand = Bitmap.createScaledBitmap(this.hand, 120, 120, true);
        tiv = (TouchImageView) findViewById(R.id.drawingImageView);
        prView = (ImageView) findViewById(R.id.preview);
        if (colorBitmap == null)
            colorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_close);
        grayBitmap = cloneBitmap(colorBitmap);
        //Bitmap.createBitmap(colorBitmap.getWidth(), colorBitmap.getHeight(), Bitmap.Config.ARGB_4444);//toGrayScale(colorBitmap);
        this.offsetLayout = (LinearLayout) findViewById(R.id.offsetLayout);
        this.bottomBar = (LinearLayout) findViewById(R.id.bottomBar);
        ImageView newBtn = (ImageView) findViewById(R.id.newBtn_ai);
        ImageView resetBtn = (ImageView) findViewById(R.id.resetBtn);
        ImageView undoBtn = (ImageView) findViewById(R.id.undoBtn);
        ImageView fitBtn = (ImageView) findViewById(R.id.fitBtn);
        ImageView done_Btn = (ImageView) findViewById(R.id.done_Btn);
        done_Btn.setOnClickListener(this);
        this.colorBtn = (ImageView) findViewById(R.id.colorBtn);
        this.recolorBtn = (ImageView) findViewById(R.id.recolorBtn);
        this.grayBtn = (ImageView) findViewById(R.id.grayBtn);
        this.zoomBtn = (ImageView) findViewById(R.id.zoomBtn);
        ImageView offsetOk = findViewById(R.id.offsetOk);
        this.offsetDemo = (ImageView) findViewById(R.id.offsetDemo);
        newBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        undoBtn.setOnClickListener(this);
        fitBtn.setOnClickListener(this);
        this.colorBtn.setOnClickListener(this);
        this.recolorBtn.setOnClickListener(this);
        this.grayBtn.setOnClickListener(this);
        this.zoomBtn.setOnClickListener(this);
        findViewById(R.id.pickColor).setOnClickListener(this);
        offsetOk.setOnClickListener(this);
        offsetBar = (SeekBar) findViewById(R.id.offsetBar);
        radiusBar = (SeekBar) findViewById(R.id.widthSeekBar);
        opacityBar = (SeekBar) findViewById(R.id.opacitySeekBar);
        brushView = (BrushView) findViewById(R.id.magnifyingView);
        radiusBar.setMax(200);
        brushView.setShapeRadiusRatio(((float) radiusBar.getProgress()) / ((float) radiusBar.getMax()));
        radiusBar.setProgress((int) tiv.radius);
        offsetBar.setProgress(50);
        offsetBar.setMax(100);
        opacityBar.setMax(240);
        opacityBar.setProgress(tiv.opacity);
        radiusBar.setOnSeekBarChangeListener(this);
        opacityBar.setOnSeekBarChangeListener(this);
        offsetBar.setOnSeekBarChangeListener(this);
        mSelectedImagePath = getIntent().getStringExtra("original"); //getSelectedPathFromIntent();
        /*if (mSelectedImagePath == null) {
            finish();
            return;
        }
        onPhotoTakenApp();*/
        initOnPhotoLoad();
//        this.grayBtn.performClick();
    }

    public void initOnPhotoLoad() {
        File file = new File(this.imageSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        clearTempBitmap();
        tiv.initDrawing();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("show", "no").commit();
        final Handler handler = new Handler();
        this.runnableCode = new Runnable() {
            public void run() {
                handler.postDelayed(runnableCode, 2000);
            }
        };

        recycler_view_recolr = findViewById(R.id.recyclerViewColor);
        this.recycler_view_recolr.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        recycler_view_recolr.setAdapter(colorAdapter);
        findViewById(R.id.recyclerViewHolder).setVisibility(View.GONE);
        handler.post(this.runnableCode);
    }

    private int preRecolor = Color.RED;

    private void changeColor(int color) {
        preRecolor = color;
        ColorSplashActivity colorSplashActivity = ColorSplashActivity.this;
        ColorSplashActivity.grayBitmap = colorSplashActivity.toGrayScale(ColorSplashActivity.colorBitmap);
        Canvas canvas = new Canvas(ColorSplashActivity.grayBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((color >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((color >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (color & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((color >> 24) & 255)) / 256.0f, 0.0f}));
        canvas.drawBitmap(ColorSplashActivity.grayBitmap, 0.0f, 0.0f, paint);
        ColorSplashActivity.tiv.splashBitmap = ColorSplashActivity.grayBitmap;
        ColorSplashActivity.tiv.updateRefMetrix();
        ColorSplashActivity.tiv.changeShaderBitmap();
        ColorSplashActivity.tiv.coloring = color;
    }

    public void clearTempBitmap() {
        tempDrawPathFile = new File(tempDrawPath);
        if (!tempDrawPathFile.exists()) {
            tempDrawPathFile.mkdirs();
        }
        if (tempDrawPathFile.isDirectory() || tempDrawPathFile.list() != null) {
            for (String file : tempDrawPathFile.list()) {
                new File(tempDrawPathFile, file).delete();
            }
        }
    }

    public Bitmap toGrayScale(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public Bitmap toEmptyBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(Color.BLACK);
        return createBitmap;
    }

    public Bitmap cloneBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
//        ColorMatrix colorMatrix = new ColorMatrix();
//        colorMatrix.setSaturation(0.0f);
//        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public void resfreshButtons(TextView tv) {
        ((TextView) findViewById(R.id.txt_color)).setBackgroundResource(0);
        ((TextView) findViewById(R.id.txt_gray)).setBackgroundResource(0);
        ((TextView) findViewById(R.id.txt_recolor)).setBackgroundResource(0);
        ((TextView) findViewById(R.id.txt_pantool)).setBackgroundResource(0);
        this.offsetLayout.setVisibility(View.GONE);
        this.bottomBar.setVisibility(View.VISIBLE);
        findViewById(R.id.recyclerViewHolder).setVisibility(View.GONE);
        tv.setBackgroundResource(R.drawable.txt_underline);
    }

    private boolean isRewardAchieved;

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.done_Btn) {
            saveImage();
            return;
        } else if (viewId == R.id.txt_color || viewId == R.id.colorBtn) {
            tiv.drawPaint.setXfermode(null);
            // Do nothing for color buttons
        } else if (viewId == R.id.fitBtn) {
            TouchImageView touchImageView = tiv;
            touchImageView.saveScale = 1.0f;
            touchImageView.radius = ((float) (radiusBar.getProgress() + radiusbarPlus)) / tiv.saveScale;
            brushView.setShapeRadiusRatio(((float) (radiusBar.getProgress() + radiusbarPlus)) / tiv.saveScale);
            tiv.fitScreen();
            tiv.updatePreviewPaint();
        } else if (viewId == R.id.txt_gray || viewId == R.id.grayBtn) {
            tiv.mode = 0;
            resfreshButtons(findViewById(R.id.txt_gray));
            tiv.splashBitmap = toGrayScale(colorBitmap);
            tiv.updateRefMetrix();
            tiv.changeShaderBitmap();
            tiv.drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            tiv.coloring = -2;
            return;
        } else if (viewId == R.id.txt_ai || viewId == R.id.newBtn_ai) {
            // Handle AI button functionality
            return;
        } else if (viewId == R.id.offsetOk) {
            // TODO: Offset OK logic to be implemented
        } else if (viewId == R.id.txt_recolor || viewId == R.id.recolorBtn) {
            tiv.mode = 0;
            resfreshButtons(findViewById(R.id.txt_recolor));
            findViewById(R.id.recyclerViewHolder).setVisibility(View.VISIBLE);
            changeColor(preRecolor);
            return;
        } else if (viewId == R.id.pickColor) {
            // Color Picker functionality
            return;
        } else if (viewId == R.id.resetBtn) {
            resetImage();
            return;
        } else if (viewId == R.id.undoBtn) {
            int i = tiv.currentImageIndex - 1;
            String sb2 = tempDrawPath + "/canvasLog" + i + ".jpg";
            Log.w("Current Image ", sb2);
            if (new File(sb2).exists()) {
                tiv.drawingBitmap = null;
                Options options = new Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inMutable = true;
                tiv.drawingBitmap = BitmapFactory.decodeFile(sb2, options);
                TouchImageView touchImageView2 = tiv;
                touchImageView2.setImageBitmap(touchImageView2.drawingBitmap);
                tiv.canvas.setBitmap(tiv.drawingBitmap);
                String sb3 = tempDrawPath + "canvasLog" + tiv.currentImageIndex + ".jpg";
                File file = new File(sb3);
                if (file.exists()) {
                    file.delete();
                }
                tiv.currentImageIndex--;
                vector.remove(vector.size() - 1);
            }
            return;
        } else if (viewId == R.id.txt_pantool || viewId == R.id.zoomBtn) {
            tiv.mode = 1;
            resfreshButtons(findViewById(R.id.txt_pantool));
            this.offsetLayout.setVisibility(View.VISIBLE);
            this.bottomBar.setVisibility(View.GONE);
            return;
        }
        tiv.mode = 0;
        resfreshButtons(findViewById(R.id.txt_color));
        tiv.splashBitmap = colorBitmap;
        tiv.updateRefMetrix();
        tiv.changeShaderBitmap();
        tiv.coloring = -1;

    }

    private static ImageAiFragment.OnBitmapReadyListener listener;

    public static void setBitmapReadyListener(ImageAiFragment.OnBitmapReadyListener listener) {
        ColorSplashActivity.listener = listener;
    }


    public void saveImage() {
        if (tiv.drawingBitmap != null) {
            if (listener != null) {
                listener.onBitmapReady(tiv.drawingBitmap);
                finish();
                return;
            }
            String fileName = "image_" + System.currentTimeMillis() + ".png";
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadDir, fileName);

            // Ensure the download directory exists
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                tiv.drawingBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                RLog.d("SaveImage", "Image saved successfully: " + file.getAbsolutePath());
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "No image to save", Toast.LENGTH_SHORT).show();
        }
    }


    public void saveImage(Bitmap bitmap) {
        String currentPath = this.imageSavePath + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(currentPath);
        Bitmap grayScale = toGrayScale(bitmap);
        Bitmap copy = grayScale.copy(Bitmap.Config.ARGB_8888, true);
        float width = ((float) copy.getWidth()) / ((float) tiv.drawingBitmap.getWidth());
        Canvas canvas = new Canvas(copy);
        Paint paint4 = new Paint(1);
        int i = -10;
        int r4 = 1;
        Paint paint;
        Paint paint2;
        Paint paint3 = null;
        while (vector.size() > 0) {
            MyPath myPath = (MyPath) vector.elementAt(0);
            if (i == myPath.color) {
                paint = paint4;
            } else {
                if (myPath.color == -1) {
                    paint3 = new Paint();
                    paint3.setShader(new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP));
                } else {
                    if (myPath.color == -2) {
                        paint3 = new Paint();
                        paint3.setShader(new BitmapShader(grayScale, TileMode.CLAMP, TileMode.CLAMP));
                    } else {
                        Bitmap copy2 = grayScale.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas2 = new Canvas(copy2);
                        Paint paint5 = new Paint();
                        float[] fArr = new float[20];
                        fArr[0] = ((float) ((myPath.color >> 16) & 255)) / 256.0f;
                        fArr[r4] = 0.0f;
                        fArr[2] = 0.0f;
                        fArr[3] = 0.0f;
                        fArr[4] = 0.0f;
                        fArr[5] = 0.0f;
                        fArr[6] = ((float) ((myPath.color >> 8) & 255)) / 256.0f;
                        fArr[7] = 0.0f;
                        fArr[8] = 0.0f;
                        fArr[9] = 0.0f;
                        fArr[10] = 0.0f;
                        fArr[11] = 0.0f;
                        fArr[12] = ((float) (myPath.color & 255)) / 256.0f;
                        fArr[13] = 0.0f;
                        fArr[14] = 0.0f;
                        fArr[15] = 0.0f;
                        fArr[16] = 0.0f;
                        fArr[17] = 0.0f;
                        fArr[18] = ((float) ((myPath.color >> 24) & 255)) / 256.0f;
                        fArr[19] = 0.0f;
                        paint5.setColorFilter(new ColorMatrixColorFilter(fArr));
                        canvas2.drawBitmap(grayScale, 0.0f, 0.0f, paint5);
                        Paint paint6 = new Paint(1);
                        paint6.setShader(new BitmapShader(copy2, TileMode.CLAMP, TileMode.CLAMP));
                        int i2 = myPath.color;
                        paint6.setStyle(Style.STROKE);
                        paint6.setStrokeWidth(myPath.r * width);
                        paint6.setStrokeCap(Cap.ROUND);
                        paint6.setStrokeJoin(Join.ROUND);
                        paint6.setMaskFilter(new BlurMaskFilter(width * 30.0f, Blur.NORMAL));
                        canvas.drawPath(myPath.convertPath(width), paint6);
                    }
                }
                paint2 = paint3;
                paint = paint2;
            }
            i = myPath.color;
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(myPath.r * width);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStrokeJoin(Join.ROUND);
            paint.setMaskFilter(new BlurMaskFilter(width * 30.0f, Blur.NORMAL));
            canvas.drawPath(myPath.convertPath(width), paint);
            paint4 = paint;
            r4 = 1;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            copy.compress(CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            grayScale.recycle();
            copy.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            MyMediaConnectorClient myMediaConnectorClient = new MyMediaConnectorClient(currentPath);
            MediaScannerConnection mediaScannerConnection = new MediaScannerConnection(this, myMediaConnectorClient);
            myMediaConnectorClient.setScanner(mediaScannerConnection);
            mediaScannerConnection.connect();
            /*Intent intent = new Intent(ColorSplashActivity.this, ShareActivity.class);
            intent.putExtra(Constants.KEY_URI_IMAGE, currentPath);
            startActivity(intent);*/
        }
    }

    public Bitmap green(Bitmap bitmap) {
        Bitmap operation = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int p = bitmap.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = r + 10;
                g = 0;
                b = 0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        return operation;
    }

    public void resetImage() {
    }


    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int id = seekBar.getId();
        if (id == R.id.offsetBar) {
            Bitmap copy = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(copy);
            Paint paint = new Paint(1);
            paint.setColor(Color.LTGRAY);
            canvas.drawCircle(150.0f, (float) (150 - offsetBar.getProgress()), 20.0f, paint);
            canvas.drawBitmap(this.hand, 95.0f, 150.0f, null);
            this.offsetDemo.setImageBitmap(copy);
        } else if (id == R.id.opacitySeekBar) {
            BrushView brushView2 = brushView;
            brushView2.isBrushSize = false;
            brushView2.setShapeRadiusRatio(tiv.radius);
            brushView.brushSize.setPaintOpacity(opacityBar.getProgress());
            brushView.invalidate();
            TouchImageView touchImageView = tiv;
            touchImageView.opacity = i + 15;
            touchImageView.updatePaintBrush();
        } else if (id == R.id.widthSeekBar) {
            BrushView brushView3 = brushView;
            brushView3.isBrushSize = true;
            brushView3.brushSize.setPaintOpacity(255);
            brushView.setShapeRadiusRatio(((float) (radiusBar.getProgress() + ImageUtils.dpToPx(this, radiusbarPlus))) / tiv.saveScale);
            String sb = radiusBar.getProgress() + "";
            Log.w("radious :", sb);
            brushView.invalidate();
            tiv.radius = ((float) (radiusBar.getProgress() + ImageUtils.dpToPx(this, radiusbarPlus))) / tiv.saveScale;
            tiv.updatePaintBrush();
        }
    }

    private File createImageFile() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + BuildConfig.APPLICATION_ID + "/CamPic/");
        storageDir.mkdirs();
        File image = null;
        try {
            image = new File(storageDir, getString(R.string.app_folder));
            if (image.exists()) image.delete();
            image.createNewFile();

            mSelectedOutputPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() != R.id.offsetBar) {
            brushView.setVisibility(View.VISIBLE);
            return;
        }
        this.offsetDemo.setVisibility(View.VISIBLE);
        Bitmap copy = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copy);
        Paint paint = new Paint(1);
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(150.0f, (float) (150 - offsetBar.getProgress()), 30.0f, paint);
        canvas.drawBitmap(this.hand, 95.0f, 150.0f, null);
        this.offsetDemo.setImageBitmap(copy);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.offsetBar) {
            this.offsetDemo.setVisibility(View.INVISIBLE);
        } else {
            brushView.setVisibility(View.INVISIBLE);
        }
    }

    public void onResume() {
//        this.busWrapper.register(this);
//        this.networkEvents.register();
        super.onResume();
    }

    public void onPause() {
//        this.busWrapper.unregister(this);
//        this.networkEvents.unregister();
        super.onPause();
    }

    public void onDestroy() {
        if (colorBitmap != null) {
            colorBitmap.recycle();
            colorBitmap = null;
        }

        super.onDestroy();
    }

    class ResetDialog extends Dialog {
        Context ctx;

        public ResetDialog(Context context) {
            super(context);
            this.ctx = context;
        }

        public ResetDialog(Context context, int i) {
            super(context, i);
            this.ctx = context;
        }

        protected ResetDialog(Context context, boolean z, OnCancelListener onCancelListener) {
            super(context, z, onCancelListener);
            this.ctx = context;
        }

        public void show() {
            ColorSplashActivity.grayBitmap = toGrayScale(colorBitmap);
            clearTempBitmap();
            ColorSplashActivity.tiv.initDrawing();
            ColorSplashActivity.tiv.saveScale = 1.0f;
            ColorSplashActivity.tiv.fitScreen();
            ColorSplashActivity.tiv.updatePreviewPaint();
            ColorSplashActivity.tiv.updatePaintBrush();
            grayBtn.setBackgroundColor(-1);
            zoomBtn.setBackgroundColor(-1);
            colorBtn.setBackgroundColor(getResources().getColor(R.color.selected));
            recolorBtn.setBackgroundColor(-1);
            ColorSplashActivity.vector.clear();
        }
    }

}
