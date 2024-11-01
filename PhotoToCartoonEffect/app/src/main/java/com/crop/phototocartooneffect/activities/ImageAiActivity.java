package com.crop.phototocartooneffect.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.fragments.ImageAiFragment;
import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.crop.phototocartooneffect.renderengins.apis.fashion.FashionEffect;
import com.crop.phototocartooneffect.renderengins.apis.fashion.FashionEffectService;
import com.crop.phototocartooneffect.renderengins.apis.imgtoimage.ImageToImageService;
import com.crop.phototocartooneffect.renderengins.effects.BackgroundRemoveFML;
import com.crop.phototocartooneffect.utils.RLog;

import java.util.ArrayList;

public class ImageAiActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private ArrayList<VideoFrames> bitmaps = new ArrayList<>();
    private RenderScript rs;

    private enum ImageCreationType {
        FIREBASE_ML_SEGMENTATION, IMAGE_EFFECT_IMG2IMG, IMAGE_EFFECT_FASHION
    }

    private ImageCreationType imageCreationType = ImageCreationType.IMAGE_EFFECT_IMG2IMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ai);

        rs = RenderScript.create(this);

        findViewById(R.id.img2imgCardView).setOnClickListener(v -> {
            imageCreationType = ImageCreationType.IMAGE_EFFECT_IMG2IMG;
            selectImage();
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            bitmaps.clear();
            if (data.getData() != null) {
                loadImage(data.getData(), -1);
            } else if (data.getClipData() != null) {
                //for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                loadImage(data.getClipData().getItemAt(0).getUri(), 0);
                //}
            }
        }
    }

    private void loadImage(Uri uri, int position) {
        ImageLoader.getInstance().loadBitmap(this, uri, position, (bitmap, keyValue, pos) -> {
            bitmaps.add(new VideoFrames(keyValue, pos));
            applyImageEffect(keyValue);
        });
    }

    private void applyImageEffect(String keyValue) {
        ImageEffect imageEffect = createImageEffect();

        imageEffect.applyEffectWithData(new ImageEffect.ImageEffectCallback() {
            @Override
            public void onSuccess(Bitmap result, String url) {
                RLog.e("ImageAiActivity", "Image Effect Success: " + url);
                ImageLoader.getInstance().loadBitmap(ImageAiActivity.this, url, System.currentTimeMillis() + "", (bitmap, keyValue1, position) -> {

                    showImageInFragment(keyValue, keyValue1);
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(ImageAiActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, this);
    }

    private ImageEffect createImageEffect() {
        String API_KEY= "";
        if (imageCreationType == ImageCreationType.IMAGE_EFFECT_IMG2IMG) {
            return new ImageToImageService("ultra realistic close up portrait ((beautiful pale cyberpunk female with heavy black eyeliner)), blue eyes, shaved side haircut, hyper detail, cinematic lighting, magic neon, dark red city, Canon EOS R3, nikon, f/1.4, ISO 200, 1/160s, 8K, RAW, unedited, symmetrical balance, in-frame, 8K",
                    "",API_KEY);
        } else if (imageCreationType == ImageCreationType.IMAGE_EFFECT_FASHION) {
            return new FashionEffectService(":A realistic photo of a model wearing a beautiful t-shirt", "https://pub-3626123a908346a7a8be8d9295f44e26.r2.dev/livewire-tmp/5dzoZ9qWI2FQxwceFDb3zULRtwCRmF-metaZjA5NjMyX3BhcmVudF8xXzE2NTMwMDMzODguanBn-.jpg", "https://pub-3626123a908346a7a8be8d9295f44e26.r2.dev/livewire-tmp/5BDmwvtizESFRO24uGDW1iu1u5TXhB-metaM2JmZmFkY2U5NDNkOGU3MDJhZDE0YTk2OTY2NjQ0NjYuanBn-.jpg", "upper_body", API_KEY);

        } else {
            return new BackgroundRemoveFML();
        }
    }

    private void showImageInFragment(String originalKey, String processedKey) {
        ImageAiFragment fragment = new ImageAiFragment(originalKey, processedKey);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
