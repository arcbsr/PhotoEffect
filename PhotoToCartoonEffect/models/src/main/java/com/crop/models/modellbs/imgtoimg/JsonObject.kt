package com.crop.models.modellbs.imgtoimg

import com.google.gson.Gson

class JsonObject {
    private val json = """
    {
        "key": "{{API_KEY}}",
        "prompt": "remove background",
        "negative_prompt": null,
        "init_image": "",
        "width": "512",
        "height": "512",
        "samples": "1",
        "num_inference_steps": "30",
        "safety_checker": "no",
        "temp": "yes",
        "enhance_prompt": "yes",
        "guidance_scale": 7.5,
        "strength": 0.7,
        "seed": null,
        "webhook": null,
        "track_id": null
    }
""".trimIndent()

    companion object {
        @Volatile
        private var instance: JsonObject? = null
        val textToImageRequest: TextToImageRequest? by lazy {
            JsonObject().createTestToImageRequest()
        }

        fun getInstance(): JsonObject {
            return instance ?: synchronized(this) {
                instance ?: JsonObject().also { instance = it }
            }
        }
    }

    private constructor() {

    }

    fun createTestToImageRequest(): TextToImageRequest {
        return Gson().fromJson(json, TextToImageRequest::class.java)
    }

}