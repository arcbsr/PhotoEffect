package com.crop.phototocartooneffect.renderengins.apis.imgupload

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.crop.modellbs.imgtoimg.Image2ImageRequest
import com.crop.modellbs.removebg.ImageRemoveBGRequest
import com.crop.modellbs.uploadimg.ImageUploadRequest
import com.crop.networklibs.apis.ApiClient
import com.crop.networklibs.apis.EffectResponse
import com.crop.networklibs.apis.ModelsLabApiService
import com.crop.phototocartooneffect.activities.ImageLoader
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.ImageEffect.ImageEffectCallback
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2
import com.crop.phototocartooneffect.utils.RLog
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ImageRemoveBgService(
    key: String, Context: Context
) : ImageEffect {

    //TODO: Implement the logic to apply the image-to-image effect
    private var image2ImageRequest: ImageRemoveBGRequest = ImageRemoveBGRequest("", key)
    private var apiKey = key
    private val apiService: ModelsLabApiService = ApiClient.modelsLabApiService
    private val context = Context

    override fun applyEffectWithData(callback: ImageEffectCallback, context: Context) {

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
        return true
    }

    private fun handleSuccess(
        effectResponse: EffectResponse, callback: ImageEffectCallback
    ) {
        RLog.e(
            "FashionEffectResponse: finalImageLinks ",
            effectResponse.output ?: effectResponse.outputImageLinks
        )
        ImageLoader.getInstance().loadBitmap(
            context,
            effectResponse.outputImageLinks[0],
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
        if (bitmap == null) {
            callback.onError(Exception("Bitmap is null"))
            return
        }
        callback.onStartProcess()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageBase64 = ImageLoader.getInstance().getBitmapAsBase64(bitmap)
                val imageUploadRequest =
                    ImageUploadRequest(imageBase64, apiKey)
                Log.d("imageUploadRequest:", imageUploadRequest.toString())
                val uploadResponse = apiService.applyImgUpload(imageUploadRequest)
                RLog.d("uploadResponse:", uploadResponse.body())
                if (uploadResponse.isSuccessful && uploadResponse.body() != null) {
                    val uploadResponseBody = uploadResponse.body()!!
                    val imageLink = uploadResponseBody.link
                    if (imageLink.isNotEmpty()) {
                        RLog.d("ImageLink:", imageLink)
                        image2ImageRequest = ImageRemoveBGRequest(imageLink, apiKey)
                        Log.d("imageProcessRequest:", image2ImageRequest.toString())
                        val response = apiService.applyRemoveBG(image2ImageRequest)
                        Log.d("imageProcessResponse:", response.body().toString())
                        // Handle response
                        handleResponse(response, callback)
                    } else {
                        callback.onError(Exception("Image processing failed: ${uploadResponse.message()}"))
                    }
                } else {
                    callback.onError(Exception("Image processing failed: ${uploadResponse.message()}"))
                }
                //TODO: Implement the logic to apply the image-to-image effect

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    logAndCallbackError(e, callback)
                }
            }
        }
    }
}