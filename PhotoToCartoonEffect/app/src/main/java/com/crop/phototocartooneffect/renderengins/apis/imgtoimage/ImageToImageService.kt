package com.crop.phototocartooneffect.renderengins.apis.imgtoimage

import android.content.Context
import android.graphics.Bitmap
import com.crop.modellbs.imgtoimg.Image2ImageRequest
import com.crop.networklibs.apis.ApiClient
import com.crop.networklibs.apis.EffectResponse
import com.crop.networklibs.apis.ModelsLabApiService
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.ImageEffect.ImageEffectCallback
import com.crop.phototocartooneffect.utils.RLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ImageToImageService(
    prompt: String, initImage: String, key: String
) : ImageEffect {

    //TODO: Implement the logic to apply the image-to-image effect
    private val image2ImageRequest: Image2ImageRequest = Image2ImageRequest(prompt, initImage, key)
    private val apiService: ModelsLabApiService = ApiClient.modelsLabApiService

    override fun applyEffectWithData(callback: ImageEffectCallback, context: Context) {
        RLog.d("TextToImageRequest:", image2ImageRequest)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                //TODO: Implement the logic to apply the image-to-image effect
                val response = apiService.applyimg2imgEffect(image2ImageRequest)

                // Handle response
                handleResponse(response, callback)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    logAndCallbackError(e, callback)
                }
            }
        }
    }

    private suspend fun handleResponse(
        response: Response<EffectResponse>, callback: ImageEffectCallback
    ) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful && response.body() != null) {
                val effectResponse = response.body()!!

                when (effectResponse.status) {
                    "success" -> handleSuccess(effectResponse, callback)
                    else -> logAndCallbackError(
                        Exception("Image processing failed: ${effectResponse.message}"), callback
                    )
                }
            } else {
                logAndCallbackError(
                    Exception("Failed to apply effect: ${response.message()}"), callback
                )
            }
        }
    }

    private suspend fun handleSuccess(
        effectResponse: EffectResponse, callback: ImageEffectCallback
    ) {
        RLog.e(
            "FashionEffectResponse: finalImageLinks ",
            effectResponse.output ?: effectResponse.outputImageLinks
        )
        callback.onSuccess(null, effectResponse.output[0])
    }

    private suspend fun logAndCallbackError(exception: Exception, callback: ImageEffectCallback) {
        RLog.e("Error applying effect: ", exception.message)
        callback.onError(exception)
    }

    override fun applyEffect(bitmap: Bitmap, callback: ImageEffectCallback) {
        TODO("Not yet implemented")
    }
}
