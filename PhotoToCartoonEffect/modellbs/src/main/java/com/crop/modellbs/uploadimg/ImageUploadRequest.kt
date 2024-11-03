package com.crop.modellbs.uploadimg

import com.crop.modellbs.imgtoimg.Image2ImageJsonObject.Companion.image2ImageRequest


data class ImageUploadRequest(
    val key: String?,
    val image: String?,
    val crop: String = "false",
) {
    constructor(image: String, key: String) : this(
        key = key,
        image = image,
        crop = "false"
    )
}