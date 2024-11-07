// ImageAiFragment.java
package com.crop.phototocartooneffect.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.jsibbold.zoomage.ZoomageView;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_editing, container, false);
        ZoomageView originalImageView = view.findViewById(R.id.myZoomageView);
        originalImageView.setImageBitmap(ImageLoader.getInstance().getBitmap(createBitmapKey));

        return view;
    }

    @Override
    public void applyAppBAR() {

    }
}
