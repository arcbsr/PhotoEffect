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
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;
import com.crop.phototocartooneffect.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAdapterFull extends RecyclerView.Adapter<ItemAdapterFull.ViewHolder> {

    private List<MainMenu> menuItems = new ArrayList<>();

    private class MainMenu {
        List<MenuItem> subMenuItems = new ArrayList<>();
        MenuItem bannerItem;
    }

    private Context context;
    private ItemAdapter.OnItemClickListener listener;


    public ItemAdapterFull(Context context, ItemAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        // Initialize menu items
        setData();
    }

    public void setData() {
        List<MenuItem> subMenuItems = new ArrayList<>();
        subMenuItems.add(new MenuItem(R.drawable.placeholder, "Remove Background", context.getString(R.string.demo_description), R.drawable.thumb, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.thumb3, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.thumb4, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.thumb5, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Remove Background", context.getString(R.string.demo_description), R.drawable.thumb6, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create Own Image", context.getString(R.string.demo_description), R.drawable.thumb, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Pro Editor", context.getString(R.string.demo_description), R.drawable.thumb3, ImageAiActivity.ImageCreationType.MONSTER_AI));
        subMenuItems.add(new MenuItem(R.drawable.pro_icon_24, "Create your Fashion", context.getString(R.string.demo_description), R.drawable.thumb5, ImageAiActivity.ImageCreationType.MONSTER_AI));
//        Collections.shuffle(subMenuItems);
        currentInsertionIndex = 0;
        for (MenuItem menuItem : subMenuItems) {
            addItems(menuItem);
        }

    }

    private int currentInsertionIndex = 0;

    private void addItems(MenuItem menuItem) {
        if (menuItems.size() == 0) {
            menuItems.add(new MainMenu());
        }
        if (menuItems.get(currentInsertionIndex).subMenuItems.size() == 4) {
            menuItems.get(currentInsertionIndex).bannerItem = AppSettings.DEFAULT_ITEM;
            currentInsertionIndex++;
            menuItems.add(new MainMenu());
        }
        menuItems.get(currentInsertionIndex).subMenuItems.add(menuItem);
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
        int itemWidth = screenWidth; // Adjust for padding

        // Set the width to fit columns and apply a varying height for staggered effect
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = itemWidth;
//        layoutParams.height = 600; // Random or dynamic height to create staggered effect
        layoutParams.height = (int) (itemWidth * (4.0 / 3.0));
        holder.itemView.setLayoutParams(layoutParams);
        MainMenu items = menuItems.get(position);
        holder.bind(items);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbImageView, thumbOverlayImageView, thumbImageView2, thumbImageView3, thumbImageView4, thumbImageView5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbImageView = itemView.findViewById(R.id.include_view11).findViewById(R.id.item_thumb);
            thumbOverlayImageView = itemView.findViewById(R.id.include_view11).findViewById(R.id.item_thumb_overlay);
            thumbImageView2 = itemView.findViewById(R.id.include_view12).findViewById(R.id.item_thumb);
            thumbImageView3 = itemView.findViewById(R.id.include_view21).findViewById(R.id.item_thumb);
            thumbImageView4 = itemView.findViewById(R.id.include_view22).findViewById(R.id.item_thumb);
            thumbImageView5 = itemView.findViewById(R.id.main_banner).findViewById(R.id.header_item_thumb);
        }

        public void bind(final MainMenu mainItems) {
            List<MenuItem> items = mainItems.subMenuItems;

            thumbImageView.setImageResource(items.get(0).getThumbResId());

            thumbImageView2.setImageResource(items.get(1).getThumbResId());

            if (items.size() > 0) {

                itemView.findViewById(R.id.include_view11).setVisibility(View.VISIBLE);
                thumbImageView.setImageResource(items.get(0).getThumbResId());
            } else {
                itemView.findViewById(R.id.include_view11).setVisibility(View.GONE);
            }
            if (items.size() > 1) {

                itemView.findViewById(R.id.include_view12).setVisibility(View.VISIBLE);
                thumbImageView2.setImageResource(items.get(1).getThumbResId());
            } else {
                itemView.findViewById(R.id.include_view12).setVisibility(View.GONE);
            }
            if (items.size() > 2) {
                itemView.findViewById(R.id.include_view21).setVisibility(View.VISIBLE);

                thumbImageView3.setImageResource(items.get(2).getThumbResId());
            } else {
                itemView.findViewById(R.id.include_view21).setVisibility(View.GONE);
            }
            if (items.size() > 3) {
                itemView.findViewById(R.id.include_view22).setVisibility(View.VISIBLE);
                thumbImageView4.setImageResource(items.get(3).getThumbResId());
            } else {
                itemView.findViewById(R.id.include_view22).setVisibility(View.GONE);
            }
            if (mainItems.bannerItem != null) {
                itemView.findViewById(R.id.main_banner).setVisibility(View.VISIBLE);
                thumbImageView5.setImageResource(mainItems.bannerItem.getThumbResId());
            } else {
                itemView.findViewById(R.id.main_banner).setVisibility(View.GONE);
            }
            thumbOverlayImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(items.get(0));
                        RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                    }
                }
            });
        }
    }
}
