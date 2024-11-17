package com.crop.phototocartooneffect.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.crop.phototocartooneffect.R;

public class ContactUs extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, com.crop.phototocartooneffect.R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactus_dialog, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.submitButton).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }
}