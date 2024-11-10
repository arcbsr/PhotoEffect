package com.crop.phototocartooneffect.dialogfragment;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ItemAdapter;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.AnimationUtils;

public class LoadingDialog extends DialogFragment {


    private ItemAdapter.OnItemClickListener listener;
    MenuItem item;
    Bitmap bitmap;

    public void setListener(ItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setMenuItem(MenuItem item) {
        this.item = item;
    }

    private LoadingDialog() {
        // Required empty public constructor
    }

    public static LoadingDialog newInstance(MenuItem item, Bitmap bitmap, ItemAdapter.OnItemClickListener listener) {
        LoadingDialog fragment = new LoadingDialog();
        fragment.setListener(listener);
        fragment.setMenuItem(item);
        fragment.bitmap = bitmap;
        Bundle args = new Bundle();
        // You can add arguments here if needed
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, com.crop.phototocartooneffect.R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_loading_dialog, container, false);

        setCancelable(false);
        ((ImageView) view.findViewById(R.id.item_thumb)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        ((ImageView) view.findViewById(R.id.item_thumb)).setImageBitmap(bitmap);
        ((ImageView) view.findViewById(R.id.loading_image_background)).setImageBitmap(bitmap);

        view.findViewById(R.id.loading_view_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.loading_view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) view.findViewById(R.id.loading_view_button)).setText("Analysis");
                view.findViewById(R.id.loading_view_button).setEnabled(false);
                valueAnimator = AnimationUtils.startRevealAnimation(view.findViewById(R.id.item_thumb).getWidth(),
                        view.findViewById(R.id.shiningLine), view.findViewById(R.id.item_thumb_overlay)
                );
                listener.onItemClick(item);
//                view.findViewById(R.id.loading_view_root2).setVisibility(View.GONE);
//                view.findViewById(R.id.loading_view_root).setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    ValueAnimator valueAnimator;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void dismissDialog() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        dismiss();

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
