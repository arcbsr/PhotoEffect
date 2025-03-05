package com.crop.phototocartooneffect.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.crop.phototocartooneffect.R;
import com.crop.phototocartooneffect.adapters.ChattingItemAdapter;
import com.crop.phototocartooneffect.enums.EditingCategories;
import com.crop.phototocartooneffect.imageloader.ImageLoader;
import com.crop.phototocartooneffect.models.ChattingItem;
import com.crop.phototocartooneffect.models.MenuItem;

import crocodile8.image_picker_plus.ImageFormat;
import crocodile8.image_picker_plus.ImagePickerPlus;
import crocodile8.image_picker_plus.ImageTransformation;
import crocodile8.image_picker_plus.PickRequest;
import crocodile8.image_picker_plus.PickSource;
import crocodile8.image_picker_plus.TypeFilter;

public class ChattingActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> pickMediaLauncher;
    String loadedImageIndex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        final ChattingItemAdapter adapter = new ChattingItemAdapter(this, item -> {

        });
        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String imagePath = data.getStringExtra("imagePath");
//                    ChattingItem chattingItem = new ChattingItem("asdsadsa");
//                    chattingItem.setImagePath(imagePath);

                    // Use the imagePath in ImageView

                    ImageLoader.getInstance().loadBitmapWithOriginalIndex(this, data.getData(), 0, false, (bitmap, keyValue, position) -> {
                        loadedImageIndex = keyValue;
                    });
                    // Add the ImageView to your layout or use it as needed

                }
            }
        });
        // Initialize views
        RecyclerView recyclerView = findViewById(R.id.chatRecyclerView);
        ImageButton galleryButton = findViewById(R.id.galleryButton);
        EditText chatEditText = findViewById(R.id.messageEditText);
        Button sendButton = findViewById(R.id.sendButton);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create and set the adapter
        recyclerView.setAdapter(adapter);
        ImageTransformation transformation = new ImageTransformation(1024, ImageFormat.JPEG, null);
        PickSource pickSource = PickSource.GALLERY;
        PickRequest pickRequest = new PickRequest(pickSource,    // Pick from GALLERY
                new TypeFilter(),                  // TypeFilter (null for default)
                transformation,        // Set transformation options
                false                  // allowMultipleSelection
        );
        // Set click listeners
        galleryButton.setOnClickListener(v -> {
            Intent intent = ImagePickerPlus.INSTANCE.createIntent(ChattingActivity.this, pickRequest);
            pickMediaLauncher.launch(intent);
        });

        sendButton.setOnClickListener(v -> {
            String message = chatEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                chatEditText.setText("");
                ChattingItem chattingItem = new ChattingItem(message);

                chattingItem.setOriginalImageIndex(loadedImageIndex);
                adapter.addItem(chattingItem);
            }
        });

    }
}