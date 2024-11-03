package com.crop.phototocartooneffect.adapters;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<MenuItem> menuItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ImageAiActivity.ImageCreationType imageCreationType);
    }

    public MenuAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        // Initialize menu items
        menuItems.add(new MenuItem(R.drawable.thumb, "Remove Background", context.getString(R.string.demo_description), R.drawable.thumb,
                ImageAiActivity.ImageCreationType.FIREBASE_ML_SEGMENTATION));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.thumb,
                ImageAiActivity.ImageCreationType.IMAGE_EFFECT_IMG2IMG));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.thumb,
                ImageAiActivity.ImageCreationType.MLB_BACKGROUND_REMOVE));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.thumb,
                ImageAiActivity.ImageCreationType.IMAGE_EFFECT_FASHION));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView, thumbImageView;
        private TextView titleTextView, descriptionTextView;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.item_title_icon);
            titleTextView = itemView.findViewById(R.id.item_title);
            thumbImageView = itemView.findViewById(R.id.item_thumb);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            cardView = itemView.findViewById(R.id.img2imgCardView);
        }

        public void bind(final MenuItem item) {
            iconImageView.setImageResource(item.getIconResId());
            titleTextView.setText(item.getTitle());
            descriptionTextView.setText(item.getDescription());
            thumbImageView.setImageResource(item.getThumbResId());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        //listener.onItemClick(item.getImageCreationType());
                        Log.d("MenuAdapter", "Clicked on item at position: " + position);
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
