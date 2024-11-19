package com.crop.phototocartooneffect.popeffect.color_splash_tool;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.dialogfragment.BaseBottomFragment;
import com.crop.phototocartooneffect.popeffect.support.ImageUtils;
import com.crop.phototocartooneffect.utils.RLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ColorSplashFragment extends BaseBottomFragment implements OnClickListener, OnSeekBarChangeListener {

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
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_color_pop, container, false);
        initializeViews(view);
        setupUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (colorBitmap == null) {
            Toast.makeText(getContext(), "Error Bitmap", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews(View view) {
        // Initialize all your views here
        imageViewContainer = view.findViewById(R.id.imageViewContainer);
        tiv = view.findViewById(R.id.drawingImageView);
        prView = view.findViewById(R.id.preview);
        offsetLayout = view.findViewById(R.id.offsetLayout);
        bottomBar = view.findViewById(R.id.bottomBar);
        colorBtn = view.findViewById(R.id.colorBtn);
        recolorBtn = view.findViewById(R.id.recolorBtn);
        grayBtn = view.findViewById(R.id.grayBtn);
        zoomBtn = view.findViewById(R.id.zoomBtn);
        offsetDemo = view.findViewById(R.id.offsetDemo);
        offsetBar = view.findViewById(R.id.offsetBar);
        radiusBar = view.findViewById(R.id.widthSeekBar);
        opacityBar = view.findViewById(R.id.opacitySeekBar);
        brushView = view.findViewById(R.id.magnifyingView);
        recycler_view_recolr = view.findViewById(R.id.recyclerViewColor);
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

    private void setupUI(View view) {
        // Setup your UI components here
        this.saveLoader = new ProgressDialog(getContext());
        vector = new Vector();
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        displayWidth = point.x;
        displayHight = point.y;
        this.hand = BitmapFactory.decodeResource(getResources(), R.drawable.hand);
        this.hand = Bitmap.createScaledBitmap(this.hand, 120, 120, true);

        if (colorBitmap == null)
            colorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_close);
        grayBitmap = cloneBitmap(colorBitmap);

        // Set click listeners
        view.findViewById(R.id.newBtn_ai).setOnClickListener(this);
        view.findViewById(R.id.resetBtn).setOnClickListener(this);
        view.findViewById(R.id.undoBtn).setOnClickListener(this);
        view.findViewById(R.id.fitBtn).setOnClickListener(this);
        view.findViewById(R.id.done_Btn).setOnClickListener(this);
        colorBtn.setOnClickListener(this);
        recolorBtn.setOnClickListener(this);
        grayBtn.setOnClickListener(this);
        zoomBtn.setOnClickListener(this);
        view.findViewById(R.id.pickColor).setOnClickListener(this);
        view.findViewById(R.id.offsetOk).setOnClickListener(this);

        // Setup SeekBars
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

        // Setup RecyclerView
        this.recycler_view_recolr.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    // ... Rest of your methods remain the same, just change 'this' to 'getContext()' or 'getActivity()' where appropriate

    @Override
    public void onDestroy() {
        if (colorBitmap != null) {
            colorBitmap.recycle();
            colorBitmap = null;
        }
        super.onDestroy();
    }

    public void saveImage() {
        if (tiv.drawingBitmap != null) {
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
                Toast.makeText(getContext(), "Image saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No image to save", Toast.LENGTH_SHORT).show();
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
            resfreshButtons(view.findViewById(R.id.txt_gray));
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
            resfreshButtons(view.findViewById(R.id.txt_recolor));
            view.findViewById(R.id.recyclerViewHolder).setVisibility(View.VISIBLE);
//            changeColor(preRecolor);
            return;
        } else if (viewId == R.id.pickColor) {
            // Color Picker functionality
            return;
        } else if (viewId == R.id.resetBtn) {
            return;
        } else if (viewId == R.id.undoBtn) {
            int i = tiv.currentImageIndex - 1;
            String sb2 = tempDrawPath + "/canvasLog" + i + ".jpg";
            Log.w("Current Image ", sb2);
            if (new File(sb2).exists()) {
                tiv.drawingBitmap = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
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
            resfreshButtons(view.findViewById(R.id.txt_pantool));
            this.offsetLayout.setVisibility(View.VISIBLE);
            this.bottomBar.setVisibility(View.GONE);
            return;
        }
        tiv.mode = 0;
        resfreshButtons(view.findViewById(R.id.txt_color));
        tiv.splashBitmap = colorBitmap;
        tiv.updateRefMetrix();
        tiv.changeShaderBitmap();
        tiv.coloring = -1;

    }

    public void resfreshButtons(TextView tv) {
        ((TextView) view.findViewById(R.id.txt_color)).setBackgroundResource(0);
        ((TextView) view.findViewById(R.id.txt_gray)).setBackgroundResource(0);
        ((TextView) view.findViewById(R.id.txt_recolor)).setBackgroundResource(0);
        ((TextView) view.findViewById(R.id.txt_pantool)).setBackgroundResource(0);
        this.offsetLayout.setVisibility(View.GONE);
        this.bottomBar.setVisibility(View.VISIBLE);
        view.findViewById(R.id.recyclerViewHolder).setVisibility(View.GONE);
        tv.setBackgroundResource(R.drawable.txt_underline);
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
            brushView.setShapeRadiusRatio(((float) (radiusBar.getProgress() + ImageUtils.dpToPx(getContext(), radiusbarPlus))) / tiv.saveScale);
            String sb = radiusBar.getProgress() + "";
            Log.w("radious :", sb);
            brushView.invalidate();
            tiv.radius = ((float) (radiusBar.getProgress() + ImageUtils.dpToPx(getContext(), radiusbarPlus))) / tiv.saveScale;
            tiv.updatePaintBrush();
        }
    }

    @Override
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

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.offsetBar) {
            this.offsetDemo.setVisibility(View.INVISIBLE);
        } else {
            brushView.setVisibility(View.INVISIBLE);
        }
    }

    // ... Rest of your code remains the same
}
