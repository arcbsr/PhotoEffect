package com.crop.phototocartooneffect.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.MenuAdapter;
import com.crop.phototocartooneffect.dialogfragment.ErrorDialog;
import com.crop.phototocartooneffect.dialogfragment.LoadingDialog;
import com.crop.phototocartooneffect.fragments.ImageAiFragment;
import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.crop.phototocartooneffect.renderengins.apis.fashion.FashionEffectService;
import com.crop.phototocartooneffect.renderengins.apis.imgtoimage.ImageToImageService;
import com.crop.phototocartooneffect.renderengins.apis.imgupload.ImageRemoveBgService;
import com.crop.phototocartooneffect.renderengins.effects.BackgroundRemoveFML;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class ImageAiActivity_backup extends AppCompatActivity implements ImageEffect.ImageEffectCallback {

    private static final int SELECT_PICTURE = 1;
    private ArrayList<VideoFrames> bitmaps = new ArrayList<>();
    private RenderScript rs;
    LoadingDialog loadingDialog;

    @Override
    public void onSuccess(Bitmap result, String key) {
        onFinished();
        if (result != null) {
            showImageInFragment("original", key);
        }
    }

    @Override
    public void onError(Exception e) {
        onFinished();
        ErrorDialog.newInstance(e.getMessage()).show(getSupportFragmentManager(), "ErrorDialog");
    }

    @Override
    public void onStartProcess() {
        if (loadingDialog != null) {
            loadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
        }

    }

    @Override
    public void onFinished() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public enum ImageCreationType {
        FIREBASE_ML_SEGMENTATION, IMAGE_EFFECT_IMG2IMG, IMAGE_EFFECT_FASHION, MLB_BACKGROUND_REMOVE
    }

    private ImageCreationType imageCreationType = ImageCreationType.FIREBASE_ML_SEGMENTATION;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ai_backup);
        loadingDialog = new LoadingDialog();
        rs = RenderScript.create(this);
//        MenuAdapter menuAdapter = new MenuAdapter(this, new MenuAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(ImageCreationType CreationType) {
//                imageCreationType = CreationType;
//                if(imageCreationType == ImageCreationType.IMAGE_EFFECT_IMG2IMG){
//                    applyImageEffect("");
//                }else {
//                    pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
//                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
//                }
//            }
//        });
//        ViewPager2 viewPager = findViewById(R.id.viewPager);
//        viewPager.setAdapter(menuAdapter);
////        viewPager.setPageTransformer(new DepthPageTransformer());
//        ((DotsIndicator) findViewById(R.id.dots_indicator)).attachTo(viewPager);
        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        loadImage(uri, 0);
                    }
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
            ((ImageView) findViewById(R.id.imageView_original)).
                    setImageBitmap(ImageLoader.getInstance().getBitmap(keyValue));
//            findViewById(R.id.imageView_original).setOnClickListener(v -> {
//                applyImageEffect(keyValue);
//            });
            applyImageEffect(keyValue);
        });
    }

    private void applyImageEffect(String keyValue) {


        ImageEffect imageEffect = createImageEffect(keyValue);
        if (imageEffect.isBitmapHolder()) {
            Bitmap bitmap = ImageLoader.getInstance().getBitmap(keyValue);
            if (bitmap == null) {
                ErrorDialog.newInstance("Image loading error").show(getSupportFragmentManager(), "ErrorDialog");
                return;
            }
            imageEffect.applyEffect(ImageLoader.getInstance().getBitmap(keyValue), this);
        } else {
            imageEffect.applyEffectWithData(this, this);
        }
    }

    private ImageEffect createImageEffect(String keyValue) {
        String API_KEY = "Kv36AX1iMgccy5D8XxXehWeEj4uqXA0wgrQlAXPovC5J4UQ4HipS5NC1lR6H";
        //"ultra realistic close up portrait ((beautiful pale cyberpunk female with heavy black eyeliner)), blue eyes, shaved side haircut, hyper detail, cinematic lighting, magic neon, dark red city, Canon EOS R3, nikon, f/1.4, ISO 200, 1/160s, 8K, RAW, unedited, symmetrical balance, in-frame, 8K",
        switch (imageCreationType) {
            case IMAGE_EFFECT_IMG2IMG:
                return new ImageToImageService("ultra realistic full body neymar", API_KEY, this);
            case IMAGE_EFFECT_FASHION:
                return new FashionEffectService(
                        ":A realistic photo of a model wearing a beautiful t-shirt",
                        "",
                        "https://pub-3626123a908346a7a8be8d9295f44e26.r2.dev/livewire-tmp/5BDmwvtizESFRO24uGDW1iu1u5TXhB-metaM2JmZmFkY2U5NDNkOGU3MDJhZDE0YTk2OTY2NjQ0NjYuanBn-.jpg",
                        "upper_body", API_KEY, this);
            case FIREBASE_ML_SEGMENTATION:
                return new BackgroundRemoveFML();
            case MLB_BACKGROUND_REMOVE:
                return new ImageRemoveBgService(API_KEY, this);
        }
        return new BackgroundRemoveFML();
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
