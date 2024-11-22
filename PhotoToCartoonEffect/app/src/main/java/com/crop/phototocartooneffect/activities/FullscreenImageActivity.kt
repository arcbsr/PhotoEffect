package com.crop.phototocartooneffect.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.crop.phototocartooneffect.R
import com.crop.phototocartooneffect.imageloader.ImageLoader

class FullscreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        val fullscreenImageView: ImageView = findViewById(R.id.fullscreenImageView)

        // Get the image resource ID or URI passed via Intent
        val imageResId = intent.getStringExtra("imageResId")
        if (imageResId != null) {
            fullscreenImageView.setImageBitmap(ImageLoader.getInstance().getBitmap(imageResId))
        }

        // Close activity on click
        fullscreenImageView.setOnClickListener { finish() }
    }

    companion object {
        const val IMAGE_LOADER_KEY: String = "imageResId"
    }
}
