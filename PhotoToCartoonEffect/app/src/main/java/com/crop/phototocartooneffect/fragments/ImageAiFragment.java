// ImageAiFragment.java
package com.crop.phototocartooneffect.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.EffectFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.MaskingView;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.popeffect.support.FilterUtils;
import com.crop.phototocartooneffect.utils.PaletteExtractor;
import com.crop.phototocartooneffect.utils.RLog;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;

import ja.burhanrashid52.photoeditor.CustomEffect;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;

public class ImageAiFragment extends BaseFragmentInterface {

    //    MaskingView maskingView;
    String originalBitmapKey, createBitmapKey;

    public static ImageAiFragment newInstance(String originalBitmapKey, String createBitmapKey) {
        ImageAiFragment fragment = new ImageAiFragment(originalBitmapKey, createBitmapKey);
        Bundle args = new Bundle();
        // You can add arguments here if needed
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageAiFragment(String originalBitmapKey, String createBitmapKey) {
        // Required empty public constructor
        this.originalBitmapKey = originalBitmapKey;
        this.createBitmapKey = createBitmapKey;
    }

    private boolean isInit = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_editing, container, false);
        new PaletteExtractor(ImageLoader.getInstance().getBitmap(originalBitmapKey), new PaletteExtractor.OnPaletteGeneratedListener() {
            @Override
            public void onPaletteGenerated(PaletteExtractor paletteExtractor) {
                init(view, ImageLoader.getInstance().getBitmap(originalBitmapKey), ImageLoader.getInstance().getBitmap(createBitmapKey), paletteExtractor);
            }
        });


//        init(view, ImageLoader.getInstance().getBitmap(originalBitmapKey), ImageLoader.getInstance().getBitmap(createBitmapKey));
        return view;
    }

    private void init(View view, Bitmap originalbitmap, Bitmap createBitmap, PaletteExtractor paletteExtractor) {


        // Change root background color
        view.setBackgroundColor(paletteExtractor.getBackgroundColor(getContext()));

        final PhotoEditorView mPhotoEditorView = view.findViewById(R.id.photoEditorView);
        mPhotoEditorView.getSource().setImageBitmap(createBitmap);
//        CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_BRIGHTNESS).setParameter("brightness", 0.5f).build();
        final PhotoEditor mPhotoEditor = new PhotoEditor.Builder(getContext(), mPhotoEditorView).build();
//        mPhotoEditor.addImage(createBitmap);
//        mPhotoEditor.setFilterEffect(customEffect);



        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String fileName = "image_" + System.currentTimeMillis() + ".png";
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File appFolder = new File(downloadDir, "PhotoToCartoonEffect");
            File file = new File(appFolder, fileName);
            // Ensure the app folder exists
            if (!appFolder.exists()) {
                appFolder.mkdirs();
            }



            view.setBackgroundColor(Color.TRANSPARENT);
            SaveSettings saveSettings = new SaveSettings.Builder()
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setClearViewsEnabled(true)
                    .build();

            mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String s) {
                    Toast.makeText(getContext(), "Image saved to " + s, Toast.LENGTH_SHORT).show();
                    RLog.e("Image saved to " + s);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void applyAppBAR(Toolbar toolbar1) {
        toolbar = toolbar1;
    }

    Toolbar toolbar;
}
