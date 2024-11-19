// ImageAiFragment.java
package com.crop.phototocartooneffect.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.popeffect.color_splash_tool.ColorSplashActivity;
import com.crop.phototocartooneffect.utils.PaletteExtractor;
import com.crop.phototocartooneffect.utils.RLog;

import java.io.File;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;

public class ImageAiFragment extends BaseFragmentInterface {
    public interface OnBitmapReadyListener {
        void onBitmapReady(Bitmap bitmap);
    }

    String originalBitmapKey, createBitmapKey;

    public static ImageAiFragment newInstance(String originalBitmapKey, String createBitmapKey) {
        ImageAiFragment fragment = new ImageAiFragment(originalBitmapKey, createBitmapKey);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageAiFragment(String originalBitmapKey, String createBitmapKey) {
        // Required empty public constructor
        this.originalBitmapKey = originalBitmapKey;
        this.createBitmapKey = createBitmapKey;
    }

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
        return view;
    }

    Bitmap editedBitmap;

    private void init(View view, Bitmap originalbitmap, Bitmap createBitmap, PaletteExtractor paletteExtractor) {

        if (originalbitmap == null) {
            Toast.makeText(getContext(), "Original bitmap is null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (createBitmap == null) {
            createBitmap = originalbitmap;
        }
        final PhotoEditorView mPhotoEditorView = view.findViewById(R.id.photoEditorView);

        // Change root background color
//        view.setBackgroundColor(paletteExtractor.getBackgroundColor(getContext()));
        mPhotoEditorView.getSource().setImageBitmap(createBitmap);
//        CustomEffect customEffect = new CustomEffect.Builder(EffectFactory.EFFECT_BRIGHTNESS).setParameter("brightness", 0.5f).build();
//        Typeface mEmojiTypeFace = Typeface.createFromAsset(getContext().getAssets(), "emojione-android.ttf");
        final PhotoEditor mPhotoEditor = new PhotoEditor.Builder(getContext(), mPhotoEditorView)
                .build();
//        mPhotoEditor.addImage(createBitmap);
//        mPhotoEditor.setFilterEffect(customEffect);

        editedBitmap = createBitmap.copy(createBitmap.getConfig(), true);
        view.findViewById(R.id.edit_current_bitmap).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ColorSplashActivity.class);
//            ColorSplashActivity.colorBitmap = finalCreateBitmap;
            ColorSplashActivity.colorBitmap = editedBitmap.copy(editedBitmap.getConfig(), true);
            startActivity(intent);

            ColorSplashActivity.setBitmapReadyListener(bitmap -> {
                editedBitmap = bitmap.copy(bitmap.getConfig(), true);
                mPhotoEditorView.getSource().setImageBitmap(editedBitmap);
                ColorSplashActivity.setBitmapReadyListener(null);
            });
        });

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
            SaveSettings saveSettings = new SaveSettings.Builder().setCompressFormat(Bitmap.CompressFormat.PNG).setClearViewsEnabled(true).build();

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
