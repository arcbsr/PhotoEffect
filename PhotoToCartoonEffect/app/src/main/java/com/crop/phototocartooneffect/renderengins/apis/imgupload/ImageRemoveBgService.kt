package com.crop.phototocartooneffect.renderengins.apis.imgupload

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.crop.modellbs.removebg.ImageFetchRequest
import com.crop.modellbs.removebg.ImageRemoveBGRequest
import com.crop.modellbs.uploadimg.ImageUploadRequest
import com.crop.networklibs.apis.ApiClient
import com.crop.networklibs.apis.EffectResponse
import com.crop.networklibs.apis.ModelsLabApiService
import com.crop.phototocartooneffect.activities.ImageLoader
import com.crop.phototocartooneffect.activities.ImageLoaderWithRetries
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
//                        pollImageProcessingStatus(effectResponse.id, callback)
//                        logAndCallbackError(
//                            Exception("Image is processing: ${effectResponse.message}"), callback
//                        )

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

    private suspend fun pollImageProcessingStatus(
        processId: Int, callback: ImageEffectCallback
    ) {
        repeat(MonsterApiClient.MAX_RETRIES) { attempt ->
            RLog.d("ImageResponse", "Waiting for process to complete... Attempt $attempt")

            val imageFetchReq = ImageFetchRequest(apiKey)
            val imgResponse = apiService.getImageProcessingStatus(
                processId = processId.toString(), request = imageFetchReq
            )
            RLog.d("Rafiur>> ", "response:  " + imgResponse.body())
            val status = imgResponse.body()?.status
            RLog.d("Rafiur>> ", "Status: $status")
            if (imgResponse.isSuccessful) {
                when (status) {
                    "success" -> {
                        handleResponse(imgResponse, callback)
                        return
                    }

                    "processing" -> {
                        RLog.d(
                            "ImageResponse",
                            "Processing in progress, retrying in ${MonsterApiClient.RETRY_DELAY} ms"
                        )
                        delay(MonsterApiClient.RETRY_DELAY)
                    }

                    else -> {
                        callback.onError(Exception("Unexpected status: $status"))
                        return
                    }
                }
            } else {
                callback.onError(Exception("Network error or unsuccessful response"))
                return
            }
        }
        callback.onError(Exception("Max retries reached without completion"))
    }

    override fun isBitmapHolder(): Boolean {
        return true
    }

    //    private fun handleSuccess(
//        effectResponse: EffectResponse, callback: ImageEffectCallback
//    ) {
//        RLog.e(
//            "FashionEffectResponse: finalImageLinks ",
//            effectResponse.output ?: effectResponse.outputImageLinks
//        )
//        ImageLoader.getInstance().loadBitmap(context,
//            effectResponse.outputImageLinks[0],
//            System.currentTimeMillis().toString(),
//            object : OnImageLoadedListener2 {
//                override fun onImageLoaded(bitmap: Bitmap?, keyValue: String?, position: Int) {
//                    callback.onSuccess(bitmap, keyValue)
//                }
//
//                override fun onErrorLoaded(url: String?, position: Int) {
//                    callback.onError(Exception("Failed to load image"))
//                }
//
//            })
//    }
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageBase64 = ImageLoader.getInstance().getBitmapAsBase64(bitmap)
                val imageUploadRequest = ImageUploadRequest(imageBase64, apiKey)
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
//                        if (response.isSuccessful) {
//                            val data = response.body();
//                            when (val status = data?.status) {
//                                "success" -> {
//                                    handleResponse(response, callback)
//                                }
//
//                                "processing" -> {
//                                    RLog.d(
//                                        "ImageResponse",
//                                        "Processing in progress, retrying in ${MonsterApiClient.RETRY_DELAY} ms"
//                                    )
//                                    delay(MonsterApiClient.INITIAL_DELAY)
////                                    pollImageProcessingStatus(118428302, callback)
////                                    pollImageProcessingStatus(data.id!!, callback)
//                                }
//
//                                else -> {
//                                    callback.onError(Exception("Unexpected status: $status"))
//
//                                }
//                            }
//                        } else {
//                            callback.onError(Exception("Network error or unsuccessful response"))
//                        }
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
