package com.crop.phototocartooneffect.dialogfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.crop.phototocartooneffect.utils.PreferencesUtils;

public class BaseDialogFragment extends DialogFragment {

    public PreferencesUtils getPreferencesUtils() {
        return PreferencesUtils.getInstance(getContext());
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
