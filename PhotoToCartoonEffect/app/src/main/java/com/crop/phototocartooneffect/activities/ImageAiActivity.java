package com.crop.phototocartooneffect.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ItemAdapter;
import com.crop.phototocartooneffect.adapters.ItemAdapterFull;
import com.crop.phototocartooneffect.adapters.MenuAdapter;
import com.crop.phototocartooneffect.animations.DepthPageTransformer;
import com.crop.phototocartooneffect.dialogfragment.ErrorDialog;
import com.crop.phototocartooneffect.dialogfragment.LoadingDialog;
import com.crop.phototocartooneffect.fragments.ImageAiFragment;
import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.crop.phototocartooneffect.renderengins.apis.fashion.FashionEffectService;
import com.crop.phototocartooneffect.renderengins.apis.imgtoimage.ImageToImageService;
import com.crop.phototocartooneffect.renderengins.apis.imgupload.ImageRemoveBgService;
import com.crop.phototocartooneffect.renderengins.apis.monster.MonsterApiClient;
import com.crop.phototocartooneffect.renderengins.effects.BackgroundRemoveFML;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

import crocodile8.image_picker_plus.ImageFormat;
import crocodile8.image_picker_plus.ImagePickerPlus;
import crocodile8.image_picker_plus.ImageTransformation;
import crocodile8.image_picker_plus.PickRequest;
import crocodile8.image_picker_plus.PickSource;
import crocodile8.image_picker_plus.TypeFilter;

public class ImageAiActivity extends AppCompatActivity implements ImageEffect.ImageEffectCallback {

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
        FIREBASE_ML_SEGMENTATION, IMAGE_EFFECT_IMG2IMG, IMAGE_EFFECT_FASHION, MLB_BACKGROUND_REMOVE,
        MONSTER_AI
    }

    private ImageCreationType imageCreationType = ImageCreationType.FIREBASE_ML_SEGMENTATION;
    private ActivityResultLauncher<Intent> pickMediaLauncher;
    private ActivityResultLauncher<PickVisualMediaRequest> pickDMediaLauncher;

    private void openImagePicker() {
        ImageTransformation transformation = new ImageTransformation(1024, ImageFormat.JPEG, null);

        PickRequest pickRequest = new PickRequest(PickSource.GALLERY,    // Pick from GALLERY
                new TypeFilter(),                  // TypeFilter (null for default)
                transformation,        // Set transformation options
                false                  // allowMultipleSelection
        );

        Intent intent = ImagePickerPlus.INSTANCE.createIntent(this, pickRequest);
        pickMediaLauncher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ai);
        loadingDialog = new LoadingDialog();
        findViewById(R.id.newButton).setOnClickListener(v -> {
            openImagePicker();
        });
        rs = RenderScript.create(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_top);
        //        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // Set up a GridLayoutManager with 2 rows (span count) and horizontal orientation
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));

        ItemAdapter adapter = new ItemAdapter(this, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageCreationType CreationType) {
                imageCreationType = CreationType;
//                if (imageCreationType == ImageCreationType.IMAGE_EFFECT_IMG2IMG) {
//                    applyImageEffect("");
//                } else {
//                    pickDMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                openImagePicker();
//                }
            }
        }); // Replace YourAdapter with your actual adapter
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView2 = findViewById(R.id.recyclerView_2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(adapter);

        RecyclerView recyclerView3 = findViewById(R.id.recyclerView_3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        recyclerView3.setAdapter(new ItemAdapterFull(this, new ItemAdapterFull.OnItemClickListener() {
            @Override
            public void onItemClick(ImageCreationType CreationType) {
                imageCreationType = CreationType;
                openImagePicker();
            }
        }));
        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (result.getData().getData() != null) {
                } else {
                    return;
                }
                if (findViewById(R.id.imageView_original).getVisibility() == View.VISIBLE) {
                    ((ImageView) findViewById(R.id.imageView_original)).setImageURI(result.getData().getData());
                    findViewById(R.id.imageView_original).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImageLoader.getInstance().loadBitmap(ImageAiActivity.this, result.getData().getData(),
                                    -1, new ImageLoader.OnImageLoadedListener() {
                                        @Override
                                        public void onImageLoaded(Bitmap bitmap, String keyValue, int position) {
                                            imageCreationType = ImageCreationType.MLB_BACKGROUND_REMOVE;
                                            loadImage(result.getData().getData(), 0);
//                                            ImageEffect imageEffect = new MonsterApiClient(
//                                                    ""
//                                                    , ImageAiActivity.this,
//                                                    "Create a fantasy avatar inspired by a mystical forest monster. The avatar should feature vibrant green skin with luminescent markings, large expressive eyes that change color based on mood, and textured, leaf-like ears. Add a flowing mane resembling vines and flowers, and give the avatar an enchanting aura with sparkling light effects surrounding it. The background should be a magical forest scene, with soft, glowing lights and whimsical plants. The overall style should be whimsical and colorful, appealing to a fantasy-loving audience."
//                                            );
//                                            imageEffect.applyEffect(bitmap, ImageAiActivity.this);
                                        }
                                    });
                        }
                    });
                } else {
                    loadImage(result.getData().getData(), 0);

                }
            }
        });

        pickDMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
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
//            ((ImageView) findViewById(R.id.imageView_original)).setImageBitmap(ImageLoader.getInstance().getBitmap(keyValue));
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
        switch (imageCreationType) {
            case IMAGE_EFFECT_IMG2IMG:
                return new ImageToImageService("Transform the image into a snack-shaped object, maintaining the original colors and textures. The result should resemble a recognizable snack item (e.g., potato chip, cookie, or candy) while preserving key features of the original image.", API_KEY, this);
            case IMAGE_EFFECT_FASHION:
                return new FashionEffectService(":A realistic photo of a model wearing a beautiful t-shirt", "", "https://pub-3626123a908346a7a8be8d9295f44e26.r2.dev/livewire-tmp/5BDmwvtizESFRO24uGDW1iu1u5TXhB-metaM2JmZmFkY2U5NDNkOGU3MDJhZDE0YTk2OTY2NjQ0NjYuanBn-.jpg", "upper_body", API_KEY, this);
            case FIREBASE_ML_SEGMENTATION:
                return new BackgroundRemoveFML();
            case MLB_BACKGROUND_REMOVE:
                return new ImageRemoveBgService(API_KEY, this);
            case MONSTER_AI:
                return new MonsterApiClient(
                        ""
                        , ImageAiActivity.this,
                        "Transform the image into a snack-shaped object. The result should look like a realistic, appetizing snack item while incorporating elements from the original image. Ensure the final image has a crisp, detailed appearance with proper lighting and textures."
                );
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