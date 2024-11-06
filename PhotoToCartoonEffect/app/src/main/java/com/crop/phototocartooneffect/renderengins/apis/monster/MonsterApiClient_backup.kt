package com.crop.phototocartooneffect.renderengins.apis.monster

import android.content.Context
import android.graphics.Bitmap
import com.crop.networklibs.apis.MonsApiClient
import com.crop.networklibs.apis.MonsterApiService
import com.crop.phototocartooneffect.imageloader.ImageLoader
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2
import com.crop.phototocartooneffect.utils.RLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class MonsterApiClient_backup(
    private val tokenText: String, private val context: Context, private val promptText: String
) : ImageEffect {
    private val apiService: MonsterApiService = MonsApiClient.monsLabApiService
    private val token = "Bearer $tokenText"
    private val prompt = RequestBody.create("text/plain".toMediaTypeOrNull(), promptText)
    private suspend fun generateImage(
        bitmap: Bitmap
    ): Response<MonsterApiService.MonsEffectResponse> {
        // Create a temporary file to store the bitmap
        val tempFile = File.createTempFile("upload_image", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)

        // Compress the bitmap and write it to the file
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val mediaType = "image/jpeg".toMediaTypeOrNull() // Set the appropriate media type
        val imagePart = MultipartBody.Part.createFormData(
            "init_image_url", tempFile.name, RequestBody.create(mediaType, tempFile)
        )
//        val prompt = RequestBody.create("text/plain".toMediaTypeOrNull(), promptText)
        return apiService.generateImage(
            "application/json", token, prompt, imagePart
        )
    }

    override fun applyEffect(bitmap: Bitmap, callback: ImageEffect.ImageEffectCallback) {
        callback.onStartProcess()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generateImage(
                    bitmap
                )
                var processId = ""
                if (response.isSuccessful && response.body()?.process_id!!.isNotEmpty()) {
                    processId = response.body()?.process_id!!
                }
                RLog.d("ImageResponse", processId)
                delay(2000)
                if (processId.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        logAndCallbackError(Exception("processId not found"), callback)
                    }
                } else {
                    var counter = 1;
                    withContext(Dispatchers.IO) {
                        while (true) {
                            RLog.d("ImageResponse", "Waiting for process to complete... $counter")
                            counter++
                            if (counter == 5) {
                                withContext(Dispatchers.Main) {
                                    logAndCallbackError(
                                        Exception("Tried for $counter times"), callback
                                    )
                                }
                                return@withContext
                            }
                            val imgResponse = apiService.getImageProcessingStatus(
                                processId = processId, authorization = token
                            )
                            RLog.d("ImageResponse request", imgResponse.body().toString())

                            if (imgResponse.isSuccessful) {
                                when (imgResponse.body()?.status) {
                                    "COMPLETED" -> {
                                        handleResponse(imgResponse, callback)
                                        return@withContext  // Exit the loop when the process is complete
                                    }

                                    "IN_PROGRESS" -> {
                                        RLog.d(
                                            "ImageResponse",
                                            "Processing is still in progress, retrying in 5 seconds..."
                                        )
                                        delay(5000) // 5-second delay before retrying
                                    }

                                    else -> {
                                        withContext(Dispatchers.Main) {
                                            logAndCallbackError(
                                                Exception(imgResponse.body()?.status), callback
                                            )
                                        }
                                        return@withContext
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    logAndCallbackError(
                                        Exception("Network error or unsuccessful response"),
                                        callback
                                    )
                                }
                                return@withContext
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    logAndCallbackError(e, callback)
                }
            }
        }
    }

    private suspend fun handleResponse(
        response: Response<MonsterApiService.ImageProcessingResponse>,
        callback: ImageEffect.ImageEffectCallback
    ) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful && response.body() != null) {
                val effectResponse = response.body()!!

                when (effectResponse.status) {
                    "COMPLETED" -> handleSuccess(effectResponse, callback)
                    "processing" -> logAndCallbackError(
                        Exception("Image is processing: ${effectResponse.status}"), callback
                    )

                    else -> logAndCallbackError(
                        Exception("Image processing failed: ${effectResponse.status}"), callback
                    )
                }
            } else {
                logAndCallbackError(
                    Exception("Failed to apply effect: ${response.message()}"), callback
                )
            }
        }
    }

    private fun handleSuccess(
        effectResponse: MonsterApiService.ImageProcessingResponse,
        callback: ImageEffect.ImageEffectCallback
    ) {
        RLog.e(
            "FashionEffectResponse: finalImageLinks ", effectResponse.result.output[0]
        )
        ImageLoader.getInstance().loadBitmap(context,
            effectResponse.result.output[0],
            System.currentTimeMillis().toString(),
            object : OnImageLoadedListener2 {
                override fun onImageLoaded(bitmap: Bitmap?, keyValue: String?, position: Int) {
                    callback.onSuccess(bitmap, keyValue)
                }

                override fun onErrorLoaded(url: String?, position: Int) {
                    callback.onError(Exception("Failed to load image"))
                }

            })
    }

    private fun logAndCallbackError(
        exception: Exception, callback: ImageEffect.ImageEffectCallback
    ) {
        RLog.e("Error applying effect: ", exception.message)
        callback.onError(exception)
    }

    override fun applyEffectWithData(callback: ImageEffect.ImageEffectCallback, context: Context?) {
        TODO("Not yet implemented")
    }

    override fun isBitmapHolder(): Boolean {
        return true
    }
}
