package com.crop.phototocartooneffect.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader;

public class AdminFragmentDialog extends BaseBottomFragment {


    private AdminFragmentDialog() {
    }

    public static AdminFragmentDialog newInstance() {
//        FireStoreImageUploader.getInstance(this).uploadImage(uri, "featured",
//        "Transform the image into a cartoon object, maintaining the original colors and textures. "
//        + "The result should resemble a recognizable snack item (e.g., potato chip, cookie, or candy)"
//        + " while preserving key features of the original image.",
//        FireStoreImageUploader.AITYPEFIREBASEDB.FEATUREAI2);
        AdminFragmentDialog fragment = new AdminFragmentDialog();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_input_dialog, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();
        });
        Spinner spinner = view.findViewById(R.id.typeSpinner);
        ArrayAdapter<FireStoreImageUploader.AITYPEFIREBASEDB> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                FireStoreImageUploader.AITYPEFIREBASEDB.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}