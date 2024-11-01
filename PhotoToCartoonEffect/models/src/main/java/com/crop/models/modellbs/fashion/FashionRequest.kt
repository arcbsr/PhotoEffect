package com.crop.models.modellbs.fashion

// Base request class for different effect requests
open class EffectRequest(
)

data class FashionEffectRequest(
    val key: String,
    val prompt: String,
    val negative_prompt: String,
    val init_image: String,
    val cloth_image: String,
    val cloth_type: String,
    val guidance_scale: Float = 7.5f,
    val num_inference_steps: Int = 21,
    val seed: Long? = null,
    val temp: String = "no",
    val webhook: String? = null,
    val track_id: String? = null
)
