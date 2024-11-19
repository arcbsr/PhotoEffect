package com.crop.phototocartooneffect.dialogfragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;

public class AdminFragmentDialog extends BaseBottomFragment {


    private AdminFragmentDialog() {
    }

    MenuItem selectedRenderItem = null;
    Uri selectedBitmapUri = null;
    Bitmap bitmap;

    public static AdminFragmentDialog newInstance(MenuItem selectedRenderItem, Uri selectedBitmap, Bitmap bitmap) {
        AdminFragmentDialog fragment = new AdminFragmentDialog();
        fragment.selectedBitmapUri = selectedBitmap;
        fragment.selectedRenderItem = selectedRenderItem;
        fragment.bitmap = bitmap;
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
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();
        });
        ((ImageView) view.findViewById(R.id.previewImageView)).setImageBitmap(bitmap);
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
        ArrayAdapter<EditingCategories.AITypeFirebaseClothTypeEDB> adapterFashion = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                EditingCategories.AITypeFirebaseClothTypeEDB.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFashion.setAdapter(adapterFashion);
        spinnerFashion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRenderItem.clothType = EditingCategories.AITypeFirebaseClothTypeEDB.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        view.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = ((EditText) view.findViewById(R.id.promptEditText)).getText().toString();
                if (prompt.isEmpty()) {
                    ((EditText) view.findViewById(R.id.promptEditText)).setError("Prompt is required");
                    return;
                }
//                FireStoreImageUploader.getInstance(this).uploadImage(uri, "featured",
//                "Transform the image into a cartoon object, maintaining the original colors and textures. "
//                + "The result should resemble a recognizable snack item (e.g., potato chip, cookie, or candy)"
//                + " while preserving key features of the original image.",
//                FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2);
                String aiType = aiTypeFirebaseDB.getValue();
                String creationtype = selectedRenderItem.getImageCreationType().getValue();
                FireStoreImageUploader.getInstance(getContext()).uploadImageToDB(bitmap, "featured",
                        prompt, aiTypeFirebaseDB, selectedRenderItem.getImageCreationType(),
                        spinnerFashion.getVisibility() == View.VISIBLE ?
                                selectedRenderItem.clothType : EditingCategories.AITypeFirebaseClothTypeEDB.NONE,
                        new FireStoreImageUploader.ImageDownloadCallback() {
                            @Override
                            public void onSuccess(String url) {
                                RLog.e("FireStoreImageUploader", "Successfully uploaded image to Firestore: " + url);
                                RLog.e("aiType", aiType);
                                RLog.e("creationtype", creationtype);
                                RLog.e("prompt", prompt);
                            }

                            @Override
                            public void onFailure(String errorMessage) {

                                RLog.e("FireStoreImageUploader", errorMessage);
                            }
                        });
            }
        });
    }
}