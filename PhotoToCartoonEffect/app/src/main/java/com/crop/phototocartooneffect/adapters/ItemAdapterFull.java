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
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.repositories.AppResources;
import com.crop.phototocartooneffect.utils.AppSettings;
import com.crop.phototocartooneffect.utils.RLog;
import com.crop.phototocartooneffect.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapterFull extends RecyclerView.Adapter<ItemAdapterFull.ViewHolder> {

    private List<MainMenu> menuItems = new ArrayList<>();

    private class MainMenu {
        List<MenuItem> subMenuItems = new ArrayList<>();
        MenuItem bannerItem;
    }

    private Context context;
    private ItemAdapter.OnItemClickListener listener;
    List<MenuItem> subMenuBannerItems;

    public ItemAdapterFull(Context context, ItemAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        // Initialize menu items
        setData();
    }

    public void setData() {
        List<MenuItem> subMenuItems = AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.USERCREATIONS);
        subMenuBannerItems = AppResources.getInstance().getItems(EditingCategories.AITypeFirebaseEDB.USERCREATIONS_BANNER);
        RLog.d("subMenuItems", subMenuBannerItems.size() + "");
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
            currentInsertionIndex++;
            menuItems.add(new MainMenu());
        }
        menuItems.get(currentInsertionIndex).subMenuItems.add(menuItem);
        if (subMenuBannerItems.size() > currentInsertionIndex) {
            menuItems.get(currentInsertionIndex).bannerItem = subMenuBannerItems.get(currentInsertionIndex);
        }
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
        layoutParams.width = itemWidth - ScreenUtils.getDevicePixelWidth(20);
//        layoutParams.height = 600; // Random or dynamic height to create staggered effect
        layoutParams.height = (int) (itemWidth * (4.0 / 3.0));
        holder.itemView.setLayoutParams(layoutParams);
        MainMenu items = menuItems.get(position);

//        createItemSize(holder.itemView.findViewById(R.id.include_view11));
        holder.bind(items);
    }

    private void createItemSize(View view) {
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // Adjust for padding
        int itemWidth = screenWidth; // Adjust for padding
        ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
        layoutParams2.width = itemWidth / 2;
        layoutParams2.height = (int) ((itemWidth / 2) * (4.0 / 3.0));
        view.setLayoutParams(layoutParams2);

    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbImageView, thumbImageView2, thumbImageView3, thumbImageView4, thumbImageView5;
        private ImageView thumbOverlayImageView, thumbOverlayImageView2, thumbOverlayImageView3, thumbOverlayImageView4, thumbOverlayImageView5;
        private View proView, proView2, proView3, proView4, proView5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbImageView = itemView.findViewById(R.id.include_view11).findViewById(R.id.item_thumb);
            thumbOverlayImageView = itemView.findViewById(R.id.include_view11).findViewById(R.id.item_thumb_overlay);
            thumbImageView2 = itemView.findViewById(R.id.include_view12).findViewById(R.id.item_thumb);
            thumbOverlayImageView2 = itemView.findViewById(R.id.include_view12).findViewById(R.id.item_thumb_overlay);
            thumbImageView3 = itemView.findViewById(R.id.include_view21).findViewById(R.id.item_thumb);
            thumbOverlayImageView3 = itemView.findViewById(R.id.include_view21).findViewById(R.id.item_thumb_overlay);
            thumbImageView4 = itemView.findViewById(R.id.include_view22).findViewById(R.id.item_thumb);
            thumbOverlayImageView4 = itemView.findViewById(R.id.include_view22).findViewById(R.id.item_thumb_overlay);
            thumbImageView5 = itemView.findViewById(R.id.main_banner).findViewById(R.id.header_item_thumb);

            proView = itemView.findViewById(R.id.include_view11).findViewById(R.id.pro_icon_view);
            proView2 = itemView.findViewById(R.id.include_view12).findViewById(R.id.pro_icon_view);
            proView3 = itemView.findViewById(R.id.include_view21).findViewById(R.id.pro_icon_view);
            proView4 = itemView.findViewById(R.id.include_view22).findViewById(R.id.pro_icon_view);
            proView5 = itemView.findViewById(R.id.main_banner).findViewById(R.id.pro_icon_view);
        }

        public void bind(final MainMenu mainItems) {
            List<MenuItem> items = mainItems.subMenuItems;
            proView.setVisibility(View.GONE);
            proView2.setVisibility(View.GONE);
            proView3.setVisibility(View.GONE);
            proView4.setVisibility(View.GONE);
            proView5.setVisibility(View.GONE);
            if (items.size() > 0) {
                if (items.get(0).isPro) {
                    proView.setVisibility(View.VISIBLE);
                }
                itemView.findViewById(R.id.include_view11).setVisibility(View.VISIBLE);

//                thumbImageView.setImageResource(items.get(0).getThumbResId());
                Glide.with(context).load(items.get(0).imageUrl == null ? items.get(0).getThumbResId() : items.get(0).imageUrl
                ).placeholder(AppSettings.IMAGE_PLACE_HOLDER).error(AppSettings.IMAGE_PLACE_HOLDER).into(thumbImageView);
            } else {
                itemView.findViewById(R.id.include_view11).setVisibility(View.GONE);
            }
            if (items.size() > 1) {
                if (items.get(1).isPro) {
                    proView2.setVisibility(View.VISIBLE);
                }
                itemView.findViewById(R.id.include_view12).setVisibility(View.VISIBLE);
//                thumbImageView2.setImageResource(items.get(1).getThumbResId());
                Glide.with(context).load(items.get(1).imageUrl == null ? items.get(1).getThumbResId() : items.get(1).imageUrl).placeholder(AppSettings.IMAGE_PLACE_HOLDER).error(AppSettings.IMAGE_PLACE_HOLDER).into(thumbImageView2);
            } else {
                itemView.findViewById(R.id.include_view12).setVisibility(View.GONE);
            }
            if (items.size() > 2) {
                if (items.get(2).isPro) {
                    proView3.setVisibility(View.VISIBLE);
                }
                itemView.findViewById(R.id.include_view21).setVisibility(View.VISIBLE);
//                thumbImageView3.setImageResource(items.get(2).getThumbResId());

                Glide.with(context).load(items.get(2).imageUrl == null ? items.get(2).getThumbResId() : items.get(2).imageUrl).placeholder(AppSettings.IMAGE_PLACE_HOLDER).error(AppSettings.IMAGE_PLACE_HOLDER).into(thumbImageView3);
            } else {
                itemView.findViewById(R.id.include_view21).setVisibility(View.GONE);
            }
            if (items.size() > 3) {
                if (items.get(3).isPro) {
                    proView4.setVisibility(View.VISIBLE);
                }
                itemView.findViewById(R.id.include_view22).setVisibility(View.VISIBLE);
//                thumbImageView4.setImageResource(items.get(3).getThumbResId());

                Glide.with(context).load(items.get(3).imageUrl == null ? items.get(3).getThumbResId() : items.get(3).imageUrl).placeholder(AppSettings.IMAGE_PLACE_HOLDER).error(AppSettings.IMAGE_PLACE_HOLDER).into(thumbImageView4);
            } else {
                itemView.findViewById(R.id.include_view22).setVisibility(View.GONE);
            }
            if (mainItems.bannerItem != null) {
                if (mainItems.bannerItem.isPro) {
                    proView5.setVisibility(View.VISIBLE);
                }
                itemView.findViewById(R.id.main_banner).setVisibility(View.VISIBLE);
                Glide.with(context).load(mainItems.bannerItem.imageUrl).placeholder(AppSettings.IMAGE_PLACE_HOLDER_BANNER).
                        error(AppSettings.IMAGE_PLACE_HOLDER_ERROR_BANNER).into(thumbImageView5);

//                thumbImageView5.setImageResource(mainItems.bannerItem.getThumbResId());
            } else {
                itemView.findViewById(R.id.main_banner).setVisibility(View.GONE);
            }
            thumbOverlayImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(items.get(0));
                    RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                }
            });

            thumbOverlayImageView2.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(items.get(1));
                    RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                }
            });
            thumbOverlayImageView3.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(items.get(2));
                    RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                }
            });
            thumbOverlayImageView4.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(items.get(3));
                    RLog.e("MenuAdapter", "Clicked on item at position: " + position);
                }
            });
        }
    }
}
