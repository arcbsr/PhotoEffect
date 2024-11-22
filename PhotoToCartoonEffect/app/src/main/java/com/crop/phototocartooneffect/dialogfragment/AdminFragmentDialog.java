package com.crop.phototocartooneffect.dialogfragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.FullscreenImageActivity;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.fragments.BaseFragmentInterface;
import com.crop.phototocartooneffect.fragments.ImageAiFragment;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.renderengins.ImageEffect;
import com.crop.phototocartooneffect.repositories.AppResources;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;
import com.crop.phototocartooneffect.utils.ScreenUtils;

import java.util.ArrayList;

public class AdminFragmentDialog extends BaseFragmentInterface {


    private AdminFragmentDialog() {
    }

    MenuItem selectedRenderItem = null;
    Uri selectedBitmapUri = null;
    Bitmap bitmap;

    public static AdminFragmentDialog newInstance(MenuItem selectedRenderItem, Uri selectedBitmap, Bitmap bitmap) {
        AdminFragmentDialog fragment = new AdminFragmentDialog();
        fragment.selectedBitmapUri = selectedBitmap;
        fragment.selectedRenderItem = selectedRenderItem;
        if (bitmap != null) fragment.bitmap = bitmap;//ScreenUtils.createScaledBitmap(bitmap, 400);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_input_dialog, container, false);
        init(view);
        return view;
    }

    EditingCategories.AITypeFirebaseEDB aiTypeFirebaseDB = EditingCategories.AITypeFirebaseEDB.UNKNOWN;

    private void init(View view) {
        if (selectedRenderItem == null) {
            selectedRenderItem = AppSettings.DEFAULT_ITEM;
        }
//        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
////            dismiss();
//        });
        if (bitmap != null) {
            ((ImageView) view.findViewById(R.id.previewImageView)).setImageBitmap(bitmap);
        }
        Spinner spinner = view.findViewById(R.id.typeSpinner);
        ArrayAdapter<EditingCategories.AITypeFirebaseEDB> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, EditingCategories.AITypeFirebaseEDB.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                aiTypeFirebaseDB = EditingCategories.AITypeFirebaseEDB.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final Spinner spinnerFashion = view.findViewById(R.id.modelTypeDress);
        spinnerFashion.setVisibility(View.GONE);
        Spinner spinner2 = view.findViewById(R.id.modelTypeSpinner);
        ArrayAdapter<EditingCategories.ImageCreationType> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, EditingCategories.ImageCreationType.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRenderItem.setImageCreationType(EditingCategories.ImageCreationType.values()[i]);
                if (EditingCategories.ImageCreationType.values()[i] == EditingCategories.ImageCreationType.IMAGE_EFFECT_FASHION) {
                    spinnerFashion.setVisibility(View.VISIBLE);
                } else {
                    spinnerFashion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<EditingCategories.AITypeFirebaseClothTypeEDB> adapterFashion = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, EditingCategories.AITypeFirebaseClothTypeEDB.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFashion.setAdapter(adapterFashion);
        adapterFashion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFashion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRenderItem.clothType = EditingCategories.AITypeFirebaseClothTypeEDB.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        view.findViewById(R.id.previewImageView_generated).setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ((ImageView) view.findViewById(R.id.previewImageView_generated)), "imageTransition");
            Intent intent = new Intent(getContext(), FullscreenImageActivity.class);
            intent.putExtra(FullscreenImageActivity.IMAGE_LOADER_KEY, generateBitmapKey); // Pass the image resource ID
            startActivity(intent, options.toBundle());
        });

        view.findViewById(R.id.previewImageView).setOnClickListener(v -> {
            ImageLoader.getInstance().loadBitmap("adminimage", bitmap);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ((ImageView) view.findViewById(R.id.previewImageView)), "imageTransition");
            Intent intent = new Intent(getContext(), FullscreenImageActivity.class);
            intent.putExtra(FullscreenImageActivity.IMAGE_LOADER_KEY, "adminimage"); // Pass the image resource ID
            startActivity(intent, options.toBundle());
        });
        view.findViewById(R.id.prepareButton).setOnClickListener(v -> {
            String prompt = ((EditText) view.findViewById(R.id.promptEditText)).getText().toString();

            if (prompt.isEmpty()) {
//                    ((EditText) view.findViewById(R.id.promptEditText)).setError("Prompt is required");
//                    return;
                prompt = "";
            }
            selectedRenderItem.prompt = prompt;
            ImageLoader.getInstance().loadBitmap("adminimage", bitmap);
            ImageAiActivity.createImageEffect(selectedRenderItem, "adminimage", getContext(), new ImageEffect.ImageEffectCallback() {
                @Override
                public void onSuccess(Bitmap result, String key) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        onFinished();
                        if (result != null) {
                            generateBitmapKey = key;
                            bitmap = result;
                            ((ImageView) view.findViewById(R.id.previewImageView_generated)).setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(getContext(), "Error NULL", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        onFinished();
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onStartProcess() {
                    view.findViewById(R.id.animationView).setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinished() {
                    view.findViewById(R.id.animationView).setVisibility(View.GONE);
                }
            });
        });
        view.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = ((EditText) view.findViewById(R.id.promptEditText)).getText().toString();

                if (prompt.isEmpty()) {
//                    ((EditText) view.findViewById(R.id.promptEditText)).setError("Prompt is required");
//                    return;
                    prompt = "";
                }
                view.findViewById(R.id.animationView).setVisibility(View.VISIBLE);
//                FireStoreImageUploader.getInstance(this).uploadImage(uri, "featured",
//                "Transform the image into a cartoon object, maintaining the original colors and textures. "
//                + "The result should resemble a recognizable snack item (e.g., potato chip, cookie, or candy)"
//                + " while preserving key features of the original image.",
//                FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2);
                String aiType = aiTypeFirebaseDB.getValue();
                String creationType = selectedRenderItem.getImageCreationType().getValue();
                CheckBox checkbox = view.findViewById(R.id.ispro);
                String finalPrompt = prompt;
                FireStoreImageUploader.getInstance(getContext()).uploadImageToDB(checkbox.isChecked(), bitmap, "featured", prompt, aiTypeFirebaseDB, selectedRenderItem.getImageCreationType(), spinnerFashion.getVisibility() == View.VISIBLE ? selectedRenderItem.clothType : EditingCategories.AITypeFirebaseClothTypeEDB.NONE, new FireStoreImageUploader.ImageDownloadCallback() {
                    @Override
                    public void onSuccess(String url) {
                        RLog.e("FireStoreImageUploader", "Successfully uploaded image to Firestore: " + url);
                        RLog.e("aiType", aiType);
                        RLog.e("creationtype", creationType);
                        RLog.e("prompt", finalPrompt);
                        view.findViewById(R.id.animationView).setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Successfully uploaded image to Firestore: " + url, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        view.findViewById(R.id.animationView).setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed to upload image to Firestore: " + errorMessage, Toast.LENGTH_SHORT).show();
                        RLog.e("FireStoreImageUploader", errorMessage);
                    }
                });
            }
        });
        view.findViewById(R.id.btn_image_url_clear).setOnClickListener(v -> {
// Get clipboard manager
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            EditText imageUrl = view.findViewById(R.id.txt_image_Url);
            // Check if there's something to paste
//            if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(android.content.ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                ClipData clipData = clipboard.getPrimaryClip();
//                if (clipData != null && clipData.getItemCount() > 0) {
//                    // Get text from clipboard
//                    String pasteData = clipData.getItemAt(0).getText().toString();
//                    // Paste text into EditText
//                    imageUrl.setText(pasteData);
//                }
//            } else {
//                Toast.makeText(getContext(), "No text to paste!", Toast.LENGTH_SHORT).show();
//            }
            imageUrl.setText("");
        });
        view.findViewById(R.id.btn_image_url).setOnClickListener(v -> {
            EditText imageUrl = view.findViewById(R.id.txt_image_Url);
            String url = imageUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                ImageLoader.getInstance().loadBitmap(getContext(), url, "admin_url", (bitmap2, keyValue, position) -> {
                    bitmap = bitmap2;
                    ((ImageView) view.findViewById(R.id.previewImageView)).setImageBitmap(bitmap);
                }, true);
            }
        });

        AppResources.getInstance().getAllPromptsItems(getContext(), new AppResources.OnAppResourcesUpdatedListener() {
            @Override
            public void onFeaturedItemsUpdated() {
                setupPrompts(view, AppResources.getInstance().getAllPrompts());
            }
        });
    }

    private String generateBitmapKey;

    private void setupPrompts(View view, ArrayList<String> allPrompts) {

        Spinner spinner2 = view.findViewById(R.id.promptsTypeSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, allPrompts);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                ((EditText) view.findViewById(R.id.promptEditText)).setText(allPrompts.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}