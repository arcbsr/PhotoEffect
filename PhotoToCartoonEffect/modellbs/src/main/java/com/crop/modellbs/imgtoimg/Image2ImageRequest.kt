package com.crop.modellbs.imgtoimg

import com.crop.modellbs.imgtoimg.Image2ImageJsonObject.Companion.image2ImageRequest


data class Image2ImageRequest(
    val key: String?,
    val prompt: String?,
    val negative_prompt: String?,
    val init_image: String?,
    val width: String?,
    val height: String?,
    val samples: String?,
    val num_inference_steps: String?,
    val safetyChecker: Boolean?,
    val temp: Boolean?,
    val enhance_prompt: Boolean?,
    val guidanceScale: Double?,
    val strength: Double?,
    val seed: String?,
    val webhook: String?,
    val track_id: String?
) {
    constructor(prompt: String, initImage: String, key:String) : this(

        key = key,
        prompt = prompt,
        negative_prompt = image2ImageRequest!!.negative_prompt,
        init_image = initImage,
        width = image2ImageRequest!!.width,
        height = image2ImageRequest!!.height,
        samples = image2ImageRequest!!.samples,
        num_inference_steps = image2ImageRequest!!.num_inference_steps,
        safetyChecker = image2ImageRequest!!.safetyChecker,
        temp = image2ImageRequest!!.temp,
        enhance_prompt = image2ImageRequest!!.enhance_prompt,
        guidanceScale = image2ImageRequest!!.guidanceScale,
        strength = image2ImageRequest!!.strength,
        seed = image2ImageRequest!!.seed,
        webhook = image2ImageRequest!!.webhook,
        track_id = image2ImageRequest!!.track_id
    )
}