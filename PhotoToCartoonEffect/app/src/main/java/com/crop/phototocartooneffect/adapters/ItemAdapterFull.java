package com.crop.phototocartooneffect.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.activities.ImageAiActivity;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.RLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAdapterFull extends RecyclerView.Adapter<ItemAdapterFull.ViewHolder> {

    private List<MenuItem> menuItems = new ArrayList<>();

    private Context context;
    private ItemAdapter.OnItemClickListener listener;


    public ItemAdapterFull(Context context, ItemAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        // Initialize menu items
        setData();
    }

    public void setData() {
        menuItems.add(new MenuItem(R.drawable.placeholder, "Remove Background", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Remove Background", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        menuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.placeholder, ImageAiActivity.ImageCreationType.MONSTER_AI));
        Collections.shuffle(menuItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_image_full, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Calculate width based on column count and screen width
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // Adjust for padding
        int itemWidth = screenWidth / 1;

        // Set the width to fit columns and apply a varying height for staggered effect
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = itemWidth;
//        layoutParams.height = 600; // Random or dynamic height to create staggered effect
        layoutParams.height = (int) (itemWidth * (4.0 / 3.0));
        holder.itemView.setLayoutParams(layoutParams);
        MenuItem item = menuItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbImageView, thumbImageView2, thumbImageView3, thumbImageView4, thumbImageView5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbImageView = itemView.findViewById(R.id.include_view11).findViewById(R.id.item_thumb);
            thumbImageView2 = itemView.findViewById(R.id.include_view12).findViewById(R.id.item_thumb);
            thumbImageView3 = itemView.findViewById(R.id.include_view21).findViewById(R.id.item_thumb);
            thumbImageView4 = itemView.findViewById(R.id.include_view22).findViewById(R.id.item_thumb);
            thumbImageView5 = itemView.findViewById(R.id.include_view3).findViewById(R.id.item_thumb);
        }

        public void bind(final MenuItem item) {
            thumbImageView.setImageResource(item.getThumbResId());
            thumbImageView2.setImageResource(item.getThumbResId());
            thumbImageView3.setImageResource(item.getThumbResId());
            thumbImageView4.setImageResource(item.getThumbResId());
            thumbImageView5.setImageResource(item.getThumbResId());
            thumbImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(item);
                        RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                    }
                }
            });
        }
    }
}
