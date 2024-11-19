package com.crop.phototocartooneffect.renderengins.apis.imgupload

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.crop.modellbs.removebg.ImageRemoveBGRequest
import com.crop.networklibs.apis.ApiClient
import com.crop.networklibs.apis.EffectResponse
import com.crop.networklibs.apis.ModelsLabApiService
import com.crop.phototocartooneffect.firabsehelper.FireStoreImageUploader
import com.crop.phototocartooneffect.imageloader.ImageLoaderWithRetries
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.ImageEffect.ImageEffectCallback
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2
import com.crop.phototocartooneffect.renderengins.apis.monster.MonsterApiClient
import com.crop.phototocartooneffect.utils.RLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        delay(MonsterApiClient.INITIAL_DELAY)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful && response.body() != null) {
                val effectResponse = response.body()!!

                when (effectResponse.status) {
                    "success" -> handleSuccess(effectResponse, callback, true)
                    "processing" -> handleSuccess(effectResponse, callback, false)

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
        effectResponse: EffectResponse, callback: ImageEffectCallback, isLoaded: Boolean
    ) {
//        val imageUrl = isLoaded?effectResponse.outputImageLinks[0]:effectResponse.outputImageLinks[0]
        val imageUrl = if (isLoaded) {
            effectResponse.output[0]
        } else {
            effectResponse.outputImageLinks[0]
        }

        RLog.e("FashionEffectResponse: finalImageLinks >>$isLoaded  : ", imageUrl)
        ImageLoaderWithRetries(context).loadImage(imageUrl, object : OnImageLoadedListener2 {
            override fun onImageLoaded(bitmap: Bitmap?, keyValue: String?, position: Int) {
                callback.onSuccess(bitmap, keyValue)
            }

            override fun onErrorLoaded(url: String?, position: Int) {
                callback.onError(Exception("Failed to load image"))
            }
        })
    }

    private fun logAndCallbackError(exception: Exception, callback: ImageEffectCallback) {
        RLog.e("Error applying effect: ", exception.message)
        callback.onError(exception)
    }


    override fun applyEffect(bitmap: Bitmap, callback: ImageEffectCallback) {
        if (bitmap == null) {
            callback.onError(Exception("Bitmap is null"))
            return
        }
        callback.onStartProcess()
        FireStoreImageUploader.getInstance(context)
            .uploadImage(bitmap, "temp", object : FireStoreImageUploader.ImageDownloadCallback {
                override fun onSuccess(imageLink: String) {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            if (imageLink.isNotEmpty()) {
                                RLog.d("ImageLink:", imageLink)
                                image2ImageRequest = ImageRemoveBGRequest(imageLink, apiKey)
                                Log.d("imageProcessRequest:", image2ImageRequest.toString())
                                val response = apiService.applyRemoveBG(image2ImageRequest)
                                Log.d("imageProcessResponse:", response.body().toString())
                                // Handle response
                                handleResponse(response, callback)
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                logAndCallbackError(e, callback)
                            }
                        }
                    }
                }

                override fun onFailure(errMsg: String) {
                    callback.onError(Exception("Image processing failed: $errMsg"))
                }
            });
    }
}
