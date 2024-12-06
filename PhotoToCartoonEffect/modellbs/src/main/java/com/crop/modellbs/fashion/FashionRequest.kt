package com.crop.modellbs.fashion


// Base request class for different effect requests
open class EffectRequest(
)

data class FashionEffectRequest(
    val key: String?,
    var prompt: String?,
    val negative_prompt: String?,
    var init_image: String?,
    val cloth_image: String?,
    val cloth_type: String?,
    val guidance_scale: Float = 7.5f,
    val num_inference_steps: Int = 21,
    val seed: Long? = null,
    val temp: String = "no",
    val webhook: String? = null,
    val track_id: String? = null
) {
    constructor(
        prompt: String, initImage: String, cloth: String, clothType: String, key: String
    ) : this(
        key = key,
        prompt = prompt,
        negative_prompt = FasonJsonObject.data!!.negative_prompt,
        init_image = initImage,
        cloth_image = cloth,
        cloth_type = clothType,
        guidance_scale = FasonJsonObject.data!!.guidance_scale,
        num_inference_steps = FasonJsonObject.data!!.num_inference_steps,
        seed = FasonJsonObject.data!!.seed,
        temp = FasonJsonObject.data!!.temp,
        webhook = FasonJsonObject.data!!.webhook,
        track_id = FasonJsonObject.data!!.track_id
    )
}
