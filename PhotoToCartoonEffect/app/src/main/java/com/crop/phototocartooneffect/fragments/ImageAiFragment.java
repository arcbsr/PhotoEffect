// ImageAiFragment.java
package com.crop.phototocartooneffect.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.activities.ImageLoader;
import com.crop.phototocartooneffect.activities.MaskingView;

public class ImageAiFragment extends Fragment {

    MaskingView maskingView;

    String originalBitmapKey, createBitmapKey;

    public ImageAiFragment(String originalBitmapKey, String createBitmapKey) {
        // Required empty public constructor
        this.originalBitmapKey = originalBitmapKey;
        this.createBitmapKey = createBitmapKey;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_editing, container, false);
        maskingView = view.findViewById(R.id.maskingView);
        view.findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maskingView.clear();
            }
        });
        view.findViewById(R.id.undoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maskingView.undoMask();
            }
        });
        maskingView.setOriginalBitmap(ImageLoader.getInstance().getBitmap(originalBitmapKey));
        maskingView.setMaskBitmap(ImageLoader.getInstance().getBitmap(createBitmapKey));
        return view;
    }
}

