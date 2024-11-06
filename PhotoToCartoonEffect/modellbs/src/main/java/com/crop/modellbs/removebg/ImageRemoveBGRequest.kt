package com.crop.modellbs.removebg


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

data class ImageFetchRequest(
    val key: String?,
) {
    constructor(image: String, key: String) : this(
        key = key,
    )
}