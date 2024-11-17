package com.crop.phototocartooneffect.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MenuFragmentDialog extends BottomSheetDialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_dialog, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.closeIcon).setOnClickListener(v -> {
            dismiss();

        });
        view.findViewById(R.id.contactUsTextView).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Contact Us", Toast.LENGTH_SHORT).show();
            ContactUs contactUs = new ContactUs();
            contactUs.show(getActivity().getSupportFragmentManager(), "ContactUs");
        });
//        view.findViewById(R.id.share).setOnClickListener(v -> {
//            // Handle share action
//        });
//        view.findViewById(R.id.rate).setOnClickListener(v -> {
//            // Handle rate action
//        });
//        view.findViewById(R.id.privacy).setOnClickListener(v -> {
//            // Handle privacy action
//        });
//        view.findViewById(R.id.terms).setOnClickListener(v -> {
//            // Handle terms action
//        });
//        view.findViewById(R.id.about).setOnClickListener(v -> {
//            // Handle about action
//        });
//        view.findViewById(R.id.feedback).setOnClickListener(v -> {
//            // Handle feedback action
//        });
//        view.findViewById(R.id.share).setOnClickListener(v -> {
//            // Handle share action
//        });
//        view.findViewById(R.id.rate).setOnClickListener(v -> {
//            // Handle rate action
//        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add any additional view setup here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Perform any cleanup operations here
    }
}