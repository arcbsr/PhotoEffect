package com.crop.phototocartooneffect.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.RenderScript;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.crop.phototocartooneffect.BuildConfig;
import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ItemAdapter;
import com.crop.phototocartooneffect.dialogfragment.AdminFragmentDialog;
import com.crop.phototocartooneffect.dialogfragment.ErrorDialog;
import com.crop.phototocartooneffect.dialogfragment.LoadingDialog;
import com.crop.phototocartooneffect.dialogfragment.MenuFragmentDialog;
import com.crop.phototocartooneffect.dialogfragment.SubscriptionFragment;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;
import com.crop.phototocartooneffect.fragments.MainFragment;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.photoediting.EditImageActivity;
import com.crop.phototocartooneffect.popeffect.color_splash_tool.ColorSplashActivity;
import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.crop.phototocartooneffect.renderengins.apis.fashion.FashionEffectService;
import com.crop.phototocartooneffect.renderengins.apis.imgtoimage.ImageToImageService;
import com.crop.phototocartooneffect.renderengins.apis.imgupload.AvatarEffectService;
import com.crop.phototocartooneffect.renderengins.apis.imgupload.ImageRemoveBgService;
import com.crop.phototocartooneffect.renderengins.apis.monster.MonsterApiClient;
import com.crop.phototocartooneffect.renderengins.effects.BackgroundRemoveFML;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;

import java.util.ArrayList;

import crocodile8.image_picker_plus.ImageFormat;
import crocodile8.image_picker_plus.ImagePickerPlus;
import crocodile8.image_picker_plus.ImageTransformation;
import crocodile8.image_picker_plus.PickRequest;
import crocodile8.image_picker_plus.PickSource;
import crocodile8.image_picker_plus.TypeFilter;

public class ImageAiActivity extends AppCompatActivity implements ImageEffect.ImageEffectCallback, ItemAdapter.OnItemClickListener {

    private static final int SELECT_PICTURE = 1;
    private ArrayList<VideoFrames> bitmaps = new ArrayList<>();
    private RenderScript rs;
    LoadingDialog loadingDialog;

    @Override
    public void onSuccess(Bitmap result, String key) {
        new Handler(Looper.getMainLooper()).post(() -> {
            onFinished();
            if (result != null) {
//                BaseFragmentInterface fragment = ImageAiFragment.newInstance("original", key);
//                fragment.applyAppBAR(toolbar);
//                showImageInFragment(fragment);
//                Intent intent = new Intent(ImageAiActivity.this, ColorSplashActivity.class);
//                ColorSplashActivity.colorBitmap = ImageLoader.getInstance().getBitmap(key);
//                startActivity(intent);
                if (selectedRenderItem.getImageCreationType() == EditingCategories.ImageCreationType.FIREBASE_ML_SEGMENTATION
                        || selectedRenderItem.getImageCreationType() == EditingCategories.ImageCreationType.MLB_BACKGROUND_REMOVE) {
                    Intent intent = new Intent(this, ColorSplashActivity.class);
                    ColorSplashActivity.colorBitmap = ImageLoader.getInstance().getBitmap(key);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ImageAiActivity.this, EditImageActivity.class);
                    intent.putExtra("image_path", key);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onError(Exception e) {
        new Handler(Looper.getMainLooper()).post(() -> {
            onFinished();
            ErrorDialog.newInstance(e.getMessage()).show(getSupportFragmentManager(), "ErrorDialog");
        });

    }

    @Override
    public void onStartProcess() {

    }

    @Override
    public void onFinished() {
        if (loadingDialog != null) {
            loadingDialog.dismissDialog();
        }
    }

    @Override
    public void onItemClick(MenuItem item) {
        if (item.documentId == null) {
            Toast.makeText(this, "It's a demo data", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isAdminDeleteMode) {
            new AlertDialog.Builder(ImageAiActivity.this, R.style.CustomAlertDialogTheme).
                    setTitle("Want to delete").setMessage("Are you sure!!!").setPositiveButton("Delete", (dialog, which) -> {
                        FireStoreImageUploader.getInstance(this).deleteDataFromFireStore(item.documentId, item.imageUrl, new FireStoreImageUploader.ImageDeleteCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ImageAiActivity.this, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(ImageAiActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }).setNegativeButton("Cancel", (dialog, which) -> {
                    }).show();

            return;
        }
        selectedRenderItem = item;
        if (item.isPro) {
            findViewById(R.id.subscribeButton).performClick();
            return;
        }
        openImagePicker(pickSource);
    }

    private MenuItem selectedRenderItem;
    PickSource pickSource = PickSource.GALLERY;
    //    private ImageCreationType imageCreationType = ImageCreationType.FIREBASE_ML_SEGMENTATION;
    private ActivityResultLauncher<Intent> pickMediaLauncher;

    private void openImagePicker(PickSource pickSource) {

        ImageTransformation transformation = new ImageTransformation(1024, ImageFormat.JPEG, null);

        PickRequest pickRequest = new PickRequest(pickSource,    // Pick from GALLERY
                new TypeFilter(),                  // TypeFilter (null for default)
                transformation,        // Set transformation options
                false                  // allowMultipleSelection
        );

        PermissionAccess permissionAccess = new PermissionAccess();
        permissionAccess.requestCameraPermission(this, new PermissionAccess.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Intent intent = ImagePickerPlus.INSTANCE.createIntent(ImageAiActivity.this, pickRequest);
                pickMediaLauncher.launch(intent);
            }

            @Override
            public void onPermissionDenied() {

            }
        });
    }

    private Toolbar toolbar;
    private boolean uploadAsAdmin = false, isAdminDeleteMode = false;

    private void setForAdminMode() {

        findViewById(R.id.addnew_item).setVisibility(View.GONE);
        findViewById(R.id.delete_item).setVisibility(View.GONE);
        findViewById(R.id.refreshButton).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.delete_item)).setTextColor(Color.WHITE);
        isAdminDeleteMode = false;
        uploadAsAdmin = false;
        if (AppSettings.IS_ADMIN_MODE) {
            findViewById(R.id.addnew_item).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_item).setVisibility(View.VISIBLE);
            findViewById(R.id.refreshButton).setVisibility(View.VISIBLE);

            findViewById(R.id.refreshButton).setOnClickListener(v -> {
                ImageLoader.getInstance().clearAllCache(ImageAiActivity.this);
                startActivity(new Intent(ImageAiActivity.this, MainActivity.class));
                finish();
            });
            findViewById(R.id.delete_item).setOnClickListener(v -> {
                if (isAdminDeleteMode) {
                    isAdminDeleteMode = false;
                    ((TextView) findViewById(R.id.delete_item)).setTextColor(Color.WHITE);
                } else {
                    isAdminDeleteMode = true;
                    ((TextView) findViewById(R.id.delete_item)).setTextColor(Color.RED);
                }
            });
            findViewById(R.id.addnew_item).setOnClickListener(v -> new AlertDialog.Builder(ImageAiActivity.this, R.style.CustomAlertDialogTheme).
                    setTitle("Admin Mode").setMessage("How do you want to add new item?").setPositiveButton("URL", (dialog, which) -> {
                        AdminFragmentDialog adminFragment = AdminFragmentDialog.newInstance(selectedRenderItem, null, null);
                        //                            adminFragment.show(getSupportFragmentManager(), "AdminFragmentDialog");
                        showImageInFragment(adminFragment);
                    }).setNegativeButton("Gallery", (dialog, which) -> {
                        uploadAsAdmin = true;
                        openImagePicker(pickSource);
                    }).show());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ai);
        toolbar = findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        if (AppSettings.IS_TESTING_MODE) {
            findViewById(R.id.subscribeButton).setVisibility(View.INVISIBLE);
        }
        setForAdminMode();
        findViewById(R.id.subscribeButton).setOnClickListener(view -> {
            SubscriptionFragment fragment = new SubscriptionFragment();
            fragment.show(getSupportFragmentManager(), "SubscriptionFragment");
        });
        findViewById(R.id.newButton).setOnClickListener(v -> {
            openImagePicker(pickSource);
        });
        rs = RenderScript.create(this);
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
                            ImageLoader.getInstance().loadBitmap(ImageAiActivity.this, result.getData().getData(), -1, (bitmap, keyValue, position) -> {
                                selectedRenderItem = new MenuItem(0, "Monster", "Monster", 0, EditingCategories.ImageCreationType.MONSTER_AI_IMG_TO_IMG, "");
                                loadImage(result.getData().getData(), 0);
                            }, true);
                        }
                    });
                } else {
                    loadImage(result.getData().getData(), 0);
                }
            }
        });
        if (savedInstanceState == null) {
            showImageInFragment(MainFragment.newInstance(this));
        } else {
            finish();
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        });
    }


    private void loadImage(Uri uri, int position) {


        ImageLoader.getInstance().loadBitmap(this, uri, position, (bitmap, keyValue, pos) -> {
            bitmaps.add(new VideoFrames(keyValue, pos));
            if (uploadAsAdmin) {
                AdminFragmentDialog dialog = AdminFragmentDialog.newInstance(selectedRenderItem, uri, bitmap);
//                dialog.show(getSupportFragmentManager(), "AdminFragmentDialog");
                showImageInFragment(dialog);
                uploadAsAdmin = false;
//            FireStoreImageUploader.getInstance(this).uploadImage(uri, "featured", "Transform the image into a cartoon object, maintaining the original colors and textures. " + "The result should resemble a recognizable snack item (e.g., potato chip, cookie, or candy)" + " while preserving key features of the original image.", FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2);
                return;
            }
            applyImageEffect(keyValue);
        }, true);
    }

    private void applyImageEffect(String keyValue) {
        if (selectedRenderItem == null) {
            return;
        }
        loadingDialog = LoadingDialog.newInstance(selectedRenderItem, ImageLoader.getInstance().getBitmap(keyValue), item -> createImageEffect(selectedRenderItem, keyValue, ImageAiActivity.this, ImageAiActivity.this));
        if (loadingDialog != null) {
            loadingDialog.show(getSupportFragmentManager(), "LoadingDialog");
        }
    }

    public static void createImageEffect(MenuItem selectedRenderItem, String bitmapKeyValue, Context context, ImageEffect.ImageEffectCallback callback) {
        ImageEffect imageEffect;
        String API_KEY = BuildConfig.MLAB_API_KEY;
        String MONSTER_TOKEN = BuildConfig.MONS_TOKEN_KEY;
        switch (selectedRenderItem.getImageCreationType()) {
            case IMAGE_EFFECT_IMG2IMG:
                imageEffect = new ImageToImageService(selectedRenderItem.prompt, API_KEY, context);
                break;
            case IMAGE_EFFECT_FASHION:
                imageEffect = new FashionEffectService(selectedRenderItem.prompt, "", selectedRenderItem.imageUrl, selectedRenderItem.clothType.getValue(), API_KEY, context);
                break;
            case FIREBASE_ML_SEGMENTATION:
                imageEffect = new BackgroundRemoveFML();
                break;
            case MLB_BACKGROUND_REMOVE:
                imageEffect = new ImageRemoveBgService(API_KEY, context);
                break;
            case MLB_AI_AVATAR:
                imageEffect = new AvatarEffectService(selectedRenderItem.prompt,
                        API_KEY, context);
                break;
            case MONSTER_AI_IMG_TO_IMG:
            case MONSTER_AI_PIX_TO_PIX:
            case MONSTER_AI_PHOTO_MAKER:
                imageEffect = new MonsterApiClient(MONSTER_TOKEN, context, selectedRenderItem.prompt, selectedRenderItem.getImageCreationType());
                RLog.d("MonsterApiClient", "MonsterApiClient" + selectedRenderItem.prompt + ">>" + selectedRenderItem.getImageCreationType().getValue());
                break;
            default:
                imageEffect = new BackgroundRemoveFML();
        }
        if (imageEffect.isBitmapHolder()) {
            Bitmap bitmap = ImageLoader.getInstance().getBitmap(bitmapKeyValue);
            if (bitmap == null) {
                callback.onError(new Exception("Image loading error"));
                return;
            }
            imageEffect.applyEffect(ImageLoader.getInstance().getBitmap(bitmapKeyValue), callback);
        } else {
            imageEffect.applyEffectWithData(callback, context);
        }
    }

    private void showImageInFragment(Fragment fragment) {
        if (!(fragment instanceof MainFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Hide MainFragment if it's currently visible
            Fragment mainFragment = getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");
            if (mainFragment != null) {
                transaction.hide(mainFragment);
            }

            // Add the new fragment on top of MainFragment
            transaction.add(R.id.fragment_container, fragment).addToBackStack(null)  // Add to back stack for proper back navigation
                    .commit();

            // Show the custom back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "MAIN_FRAGMENT").commit();
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setNavigationOnClickListener(v -> openMenuScreen());
        }
    }

    private void openMenuScreen() {
        MenuFragmentDialog menuFragmentDialog = new MenuFragmentDialog();
        menuFragmentDialog.show(getSupportFragmentManager(), "MenuFragmentDialog");
//        MoreBottomFragment moreBottomFragment = new MoreBottomFragment();
//        moreBottomFragment.show(getSupportFragmentManager(), "MoreBottomFragment");
    }

    private void handleBackPress() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        RLog.e("BackPressed", "fragment count " + backStackCount);

        if (backStackCount > 0) {
            getSupportFragmentManager().popBackStack();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currentFragment instanceof MainFragment) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(null);
                toolbar.setNavigationOnClickListener(null);
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_menu);
                toolbar.setNavigationOnClickListener(v -> openMenuScreen());
            }
        } else {
            finish(); // Or call super.onBackPressed() if you want to let the system handle it
        }
    }
}