package com.crop.phototocartooneffect.dialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.utils.PreferencesUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BaseBottomFragment extends BottomSheetDialogFragment {

    public PreferencesUtils getPreferencesUtils() {
        return PreferencesUtils.getInstance(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
