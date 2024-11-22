package com.crop.modellbs.avatar


data class AvatarEffectRequest(
    val key: String?,
    val prompt: String?,
    val negative_prompt: String?,
    var init_image: String?,
) {
    constructor(
        prompt: String, initImage: String, key: String
    ) : this(
        key = key,
        prompt = prompt,
        negative_prompt = AvatarJsonObject.data!!.negative_prompt,
        init_image = initImage
    )
}
