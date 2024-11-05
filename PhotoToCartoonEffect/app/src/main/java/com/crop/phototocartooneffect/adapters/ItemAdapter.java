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
import com.crop.phototocartooneffect.utils.RLog;
import com.crop.phototocartooneffect.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<MenuItem> menuItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ImageAiActivity.ImageCreationType imageCreationType);
    }

    public ItemAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        // Initialize menu items
        menuItems.add(new MenuItem(R.drawable.thumb, "Remove Background", context.getString(R.string.demo_description), R.drawable.thumb4, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION));
//        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.thumb5, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
//        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.thumb6, ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
//        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.thumb3, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
//        menuItems.add(new MenuItem(R.drawable.thumb, "Remove Background", context.getString(R.string.demo_description), R.drawable.thumb4, ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION));
//        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.thumb5, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
//        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.thumb6, ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
//        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.thumb3, ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Calculate width based on column count and screen width
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels - 40; // Adjust for padding
        int itemWidth = screenWidth / 2;

        // Set the width to fit columns and apply a varying height for staggered effect
        ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
        layoutParams.height = Utils.getDevicePixelWidth(240); // Set height based on device pixels for staggered effect
        layoutParams.width = itemWidth;
        holder.cardView.setLayoutParams(layoutParams);
        MenuItem item = menuItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbImageView;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.item_thumb);
            cardView = itemView.findViewById(R.id.img2imgCardView);
        }

        public void bind(final MenuItem item) {
            thumbImageView.setImageResource(item.getThumbResId());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(item.getImageCreationType());
                        RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                    }
                }
            });
        }
    }

    private class MenuItem {
        private int iconResId;
        private String title;
        private String description;
        private int thumbResId;
        private ImageAiActivity.ImageCreationType imageCreationType;

        public MenuItem(int iconResId, String title, String description, int thumbResId, ImageAiActivity.ImageCreationType imageCreationType) {
            this.iconResId = iconResId;
            this.title = title;
            this.description = description;
            this.thumbResId = thumbResId;
            this.imageCreationType = imageCreationType;
        }

        public int getIconResId() {
            return iconResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getThumbResId() {
            return thumbResId;
        }

        public ImageAiActivity.ImageCreationType getImageCreationType() {
            return imageCreationType;
        }
    }
}
