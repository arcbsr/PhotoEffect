package com.crop.models.modellbs.imgtoimg

import com.crop.models.modellbs.imgtoimg.JsonObject.Companion.textToImageRequest


data class TextToImageRequest(
    val key: String?,
    val prompt: String?,
    val negative_prompt: String?,
    val init_image: String?,
    val width: String?,
    val height: String?,
    val samples: String?,
    val num_inference_steps: String?,
    val safetyChecker: String?,
    val temp: String?,
    val enhance_prompt: String?,
    val guidanceScale: Double?,
    val strength: Double?,
    val seed: String?,
    val webhook: String?,
    val track_id: String?
) {
    constructor(prompt: String, initImage: String) : this(

        key = "y2muZahlFMmxbImwFaQR5gEnUxmhGhxSEaETL5pGPqOOLarir0CxVOy1S9Dl",
        prompt = prompt,
        negative_prompt = textToImageRequest!!.negative_prompt,
        init_image = initImage,
        width = textToImageRequest!!.width,
        height = textToImageRequest!!.height,
        samples = textToImageRequest!!.samples,
        num_inference_steps = textToImageRequest!!.num_inference_steps,
        safetyChecker = textToImageRequest!!.safetyChecker,
        temp = textToImageRequest!!.temp,
        enhance_prompt = textToImageRequest!!.enhance_prompt,
        guidanceScale = textToImageRequest!!.guidanceScale,
        strength = textToImageRequest!!.strength,
        seed = textToImageRequest!!.seed,
        webhook = textToImageRequest!!.webhook,
        track_id = textToImageRequest!!.track_id
    )
}