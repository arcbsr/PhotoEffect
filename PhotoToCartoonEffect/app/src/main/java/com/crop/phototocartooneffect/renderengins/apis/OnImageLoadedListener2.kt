package com.crop.phototocartooneffect.renderengins.apis

import android.graphics.Bitmap

interface OnImageLoadedListener2 {
    fun onImageLoaded(bitmap: Bitmap?, keyValue: String?, position: Int)
    fun onErrorLoaded(url: String?, position: Int)
}
