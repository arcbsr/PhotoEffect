package com.crop.phototocartooneffect.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<MenuItem> menuItems = new ArrayList();
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public ItemAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<MenuItem> menuItems) {
        if (menuItems != null) {
            this.menuItems = menuItems;
        } else {
            RLog.d("ItemAdapter", "menuItems is null");
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_image, parent, false);
        return new ViewHolder(view);
    }

    private int columnCount = 3;
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
        private View cardView;
        private View lineView, proView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.item_thumb);
            overlayImageView = itemView.findViewById(R.id.item_thumb_overlay);
            shineImageView = itemView.findViewById(R.id.item_thumb_shine);
            cardView = itemView.findViewById(R.id.img2imgCardView);
            lineView = itemView.findViewById(R.id.shiningLine);
            proView = itemView.findViewById(R.id.pro_icon_view);
        }

        public void bind(final MenuItem item) {
            thumbImageView.setImageResource(item.getThumbResId());
            Glide.with(context).load(item.imageUrl).placeholder(AppSettings.IMAGE_PLACE_HOLDER).error(AppSettings.IMAGE_PLACE_HOLDER_ERROR).into(thumbImageView);
            proView.setVisibility(View.GONE);
            if (item.isPro) {
                proView.setVisibility(View.VISIBLE);
            }
            overlayImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(item);
                    RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                }
            });
        }
    }
}
