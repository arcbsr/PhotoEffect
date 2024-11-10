package com.crop.phototocartooneffect.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.AnimationUtils;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;
import com.crop.phototocartooneffect.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<MenuItem> menuItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public ItemAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        // Initialize menu items
        menuItems.add(AppSettings.DEFAULT_ITEM);
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Remove Background", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
    }

    public void setData() {
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Remove Background", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_image, parent, false);
        return new ViewHolder(view);
    }

    private int columnCount = 2;
    private int padding = 0;

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels - padding; // Adjust for padding
        int itemWidth = (screenWidth / columnCount);

        // Set the width to fit columns and apply a varying height for staggered effect
        ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
        layoutParams.height = (int) (itemWidth * (3.0 / 3.0)); // Set height based on aspect ratio (4:3)
        layoutParams.width = itemWidth;

        // Get margin value from resources
        int margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);

        // If LayoutParams is an instance of `ViewGroup.MarginLayoutParams`, you can directly set margins
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginParams.setMargins(margin, margin, margin, margin); // Set margin on all sides
            holder.cardView.setLayoutParams(marginParams); // Apply the updated LayoutParams
        } else {
            // If LayoutParams is not MarginLayoutParams, you'll need to cast it
            RLog.e("LayoutParams", "Unexpected LayoutParams type");
        }
        MenuItem item = menuItems.get(position);
//        AnimationUtils.shineEffectAnim(itemWidth, holder.shineImageView);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbImageView, shineImageView, overlayImageView;
        private CardView cardView;
        private View lineView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.item_thumb);
            overlayImageView = itemView.findViewById(R.id.item_thumb_overlay);
            shineImageView = itemView.findViewById(R.id.item_thumb_shine);
            cardView = itemView.findViewById(R.id.img2imgCardView);
            lineView = itemView.findViewById(R.id.shiningLine);
        }

        public void bind(final MenuItem item) {
            thumbImageView.setImageResource(item.getThumbResId());
            overlayImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(item);
//                    if (item.animator != null) {
//                        item.animator.end();
//                        item.animator = null;
//                    } else {
//                        item.animator = AnimationUtils.startRevealAnimation(v.getWidth(), lineView, overlayImageView);
//                        item.animator.start();
//                    }
                    RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                }
            });
        }
    }
}
