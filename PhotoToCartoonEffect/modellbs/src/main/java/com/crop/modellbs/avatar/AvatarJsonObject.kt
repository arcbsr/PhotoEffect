package com.crop.modellbs.avatar

import com.crop.modellbs.fashion.FashionEffectRequest
import com.google.gson.Gson

class AvatarJsonObject {
    private val json = """
    {
        "key": "{{API_KEY}}",
          "prompt": "pretty woman keep similar",
          "negative_prompt": "anime, cartoon, drawing, big nose, long nose, fat, ugly, big lips, big mouth, face proportion mismatch, unrealistic, monochrome, lowres, bad anatomy, worst quality, low quality, blurry",
          "init_image":"",
          "width": "1024",
          "height": "1024",
          "samples": "2",
          "num_inference_steps": "21",
          "safety_checker": false,
          "base64": false,
          "seed": null,
          "guidance_scale": 7.5,
          "identitynet_strength_ratio": 1.0,
          "adapter_strength_ratio": 1.0,
          "pose_strength": 0.4,
          "canny_strength": 0.3,
          "controlnet_selection":"pose",
          "webhook": null,
          "track_id": null
    }
""".trimIndent()

    companion object {
        @Volatile
        private var instance: AvatarJsonObject? = null
        val data: AvatarEffectRequest? by lazy {
            AvatarJsonObject().createTestToImageRequest()
        }

        fun getInstance(): AvatarJsonObject {
            return instance ?: synchronized(this) {
                instance ?: AvatarJsonObject().also { instance = it }
            }
        }
    }

    private constructor() {

    }

    fun createTestToImageRequest(): AvatarEffectRequest {
        return Gson().fromJson(json, AvatarEffectRequest::class.java)
    }

}