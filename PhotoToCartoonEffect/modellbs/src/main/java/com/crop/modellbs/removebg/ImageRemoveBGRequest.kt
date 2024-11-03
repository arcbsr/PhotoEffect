package com.crop.modellbs.removebg

import com.crop.modellbs.imgtoimg.Image2ImageJsonObject.Companion.image2ImageRequest


data class ImageRemoveBGRequest(
    val key: String?,
    val image: String?,
    val seed: String = "12345",
) {
    constructor(image: String, key: String) : this(
        key = key,
        image = image,
        seed = "12345"
    )
}