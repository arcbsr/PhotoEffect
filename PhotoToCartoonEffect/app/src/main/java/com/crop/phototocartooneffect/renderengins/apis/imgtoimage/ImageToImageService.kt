package com.crop.phototocartooneffect.renderengins.apis.imgtoimage

import android.content.Context
import android.graphics.Bitmap
import com.crop.modellbs.imgtoimg.Image2ImageRequest
import com.crop.networklibs.apis.ApiClient
import com.crop.networklibs.apis.EffectResponse
import com.crop.networklibs.apis.ModelsLabApiService
import com.crop.phototocartooneffect.activities.ImageLoader
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.ImageEffect.ImageEffectCallback
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2
import com.crop.phototocartooneffect.utils.RLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ImageToImageService(
    prompt: String, key: String, context: Context
) : ImageEffect {

    //TODO: Implement the logic to apply the image-to-image effect
    private val image2ImageRequest: Image2ImageRequest = Image2ImageRequest(prompt, key)
    private val apiService: ModelsLabApiService = ApiClient.modelsLabApiService
    private val context = context
    override fun applyEffectWithData(callback: ImageEffectCallback, context: Context) {
        callback.onStartProcess()
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
                    "processing" -> logAndCallbackError(
                        Exception("Image is processing: ${effectResponse.message}"), callback
                    )

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

    override fun isBitmapHolder(): Boolean {
        return false
    }

    private fun handleSuccess(
        effectResponse: EffectResponse, callback: ImageEffectCallback
    ) {
        RLog.e(
            "FashionEffectResponse: finalImageLinks ",
            effectResponse.output ?: effectResponse.outputImageLinks
        )
//        callback.onSuccess(null, effectResponse.output[0])
        ImageLoader.getInstance().loadBitmap(
            context,
            effectResponse.output[0],
            System.currentTimeMillis().toString(),
            object : OnImageLoadedListener2 {
                override fun onImageLoaded(bitmap: Bitmap?, keyValue: String?, position: Int) {
                    callback.onSuccess(bitmap, keyValue)
                }

                override fun onErrorLoaded(url: String?, position: Int) {
                    callback.onError(Exception("Failed to load image"))
                }

            }
        )
    }

    private suspend fun logAndCallbackError(exception: Exception, callback: ImageEffectCallback) {
        RLog.e("Error applying effect: ", exception.message)
        callback.onError(exception)
    }

    override fun applyEffect(bitmap: Bitmap, callback: ImageEffectCallback) {
        TODO("Not yet implemented")
    }
}
