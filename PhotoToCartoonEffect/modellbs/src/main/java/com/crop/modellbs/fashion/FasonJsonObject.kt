package com.crop.modellbs.fashion

import com.google.gson.Gson

class FasonJsonObject {
    private val json = """
    {
        "key": "{{API_KEY}}",
        "prompt": "remove background",
        "negative_prompt": "Low quality, unrealistic, bad cloth",
        "init_image": "",
        "cloth_image": "",
        "cloth_type": "",
        "guidance_scale": 7.5,
        "num_inference_steps" : 21,
        "safety_checker": "no",
        "seed": null,
        "temp": "no",
        "webhook": null,
        "track_id": null
    }
""".trimIndent()

    companion object {
        @Volatile
        private var instance: FasonJsonObject? = null
        val data: FashionEffectRequest? by lazy {
            FasonJsonObject().createTestToImageRequest()
        }

        fun getInstance(): FasonJsonObject {
            return instance ?: synchronized(this) {
                instance ?: FasonJsonObject().also { instance = it }
            }
        }
    }

    private constructor() {

    }

    fun createTestToImageRequest(): FashionEffectRequest {
        return Gson().fromJson(json, FashionEffectRequest::class.java)
    }

}