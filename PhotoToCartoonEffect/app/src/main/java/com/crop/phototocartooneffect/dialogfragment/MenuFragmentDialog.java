package com.crop.phototocartooneffect.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crop.phototocartooneffect.R;

public class MenuFragmentDialog extends BaseBottomFragment {


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
            ContactUsFragment contactUsFragment = new ContactUsFragment();
            if (!contactUsFragment.hasContactedToday(getContext())) {
                contactUsFragment.show(getActivity().getSupportFragmentManager(), "ContactUs");
            } else {
                Toast.makeText(getContext(), "You can only contact us once per day", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.termsTextView).setOnClickListener(v -> {
            // Handle share action
        });
        view.findViewById(R.id.rateUsTextView).setOnClickListener(v -> {
            // Handle rate action
        });
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
}