package com.crop.phototocartooneffect.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.models.ChattingItem;
import com.crop.phototocartooneffect.models.MenuItem;
import com.crop.phototocartooneffect.utils.RLog;

import java.util.ArrayList;
import java.util.List;

public class ChattingItemAdapter extends RecyclerView.Adapter<ChattingItemAdapter.ViewHolder> {

    private List<ChattingItem> chattingItems = new ArrayList();
    private Context context;
    private OnItemClickListener listener;

    public ChattingItemAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public void addItem(ChattingItem item) {
        chattingItems.add(item);
        notifyItemInserted(chattingItems.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_chatting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChattingItem item = chattingItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return chattingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View cardView;
        private ImageView imageViewOriginal;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewBottomView);
            imageViewOriginal = itemView.findViewById(R.id.imageViewOriginal);

        }

        public void bind(final ChattingItem item) {
            textView.setText(item.getMessage());
            RLog.d("ChattingItemAdapter", "item.getOriginalImageIndex() = " + item.getOriginalImageIndex());
            ImageLoader.getInstance().getBitmap(item.getOriginalImageIndex(), new ImageLoader.OnImageLoadedListener() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String keyValue, int position) {
                    imageViewOriginal.setImageBitmap(bitmap);
                }
            });
        }
    }
}
