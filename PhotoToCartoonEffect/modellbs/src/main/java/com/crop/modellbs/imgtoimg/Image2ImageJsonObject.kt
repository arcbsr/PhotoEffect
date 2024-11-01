package com.crop.modellbs.imgtoimg

import com.google.gson.Gson

class Image2ImageJsonObject {
    private val json = """
    {
        "key": "{{API_KEY}}",
        "prompt": "remove background",
        "negative_prompt": "((out of frame)), ((extra fingers)), mutated hands, ((poorly drawn hands)), ((poorly drawn face)), (((mutation))), (((deformed))), (((tiling))), ((naked)), ((tile)), ((fleshpile)), ((ugly)), (((abstract))), blurry, ((bad anatomy)), ((bad proportions)), ((extra limbs)), cloned face, (((skinny))), glitchy, ((extra breasts)), ((double torso)), ((extra arms)), ((extra hands)), ((mangled fingers)), ((missing breasts)), (missing lips), ((ugly face)), ((fat)), ((extra legs))",
        "init_image": "",
        "width": "512",
        "height": "512",
        "samples": "1",
        "num_inference_steps": "30",
        "safety_checker": false,
        "temp": false,
        "enhance_prompt": false,
        "guidance_scale": 7.5,
        "strength": 0.7,
        "seed": null,
        "webhook": null,
        "track_id": null
    }
""".trimIndent()

    companion object {
        @Volatile
        private var instance: Image2ImageJsonObject? = null
        val image2ImageRequest: Image2ImageRequest? by lazy {
            Image2ImageJsonObject().createTestToImageRequest()
        }

        fun getInstance(): Image2ImageJsonObject {
            return instance ?: synchronized(this) {
                instance ?: Image2ImageJsonObject().also { instance = it }
            }
        }
    }

    private constructor() {

    }

    fun createTestToImageRequest(): Image2ImageRequest {
        return Gson().fromJson(json, Image2ImageRequest::class.java)
    }

}