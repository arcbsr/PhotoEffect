package com.crop.phototocartooneffect.renderengins.apis.monster

import android.content.Context
import android.graphics.Bitmap
import com.crop.networklibs.apis.MonsApiClient
import com.crop.networklibs.apis.MonsterApiService
import com.crop.phototocartooneffect.activities.ImageLoader
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.apis.OnImageLoadedListener2
import com.crop.phototocartooneffect.utils.RLog
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class MonsterApiClient(
    tokenText: String, private val context: Context, promptText: String
) : ImageEffect {

    private val apiService: MonsterApiService = MonsApiClient.monsLabApiService
    private val token = "Bearer $tokenText"
    private val prompt = RequestBody.create("text/plain".toMediaTypeOrNull(), promptText)

    companion object {
        private const val MAX_RETRIES = 5
        private const val RETRY_DELAY = 5000L
        private const val INITIAL_DELAY = 2000L
        private const val MEDIA_TYPE_IMAGE = "image/jpeg"
    }

    override fun applyEffect(bitmap: Bitmap, callback: ImageEffect.ImageEffectCallback) {
        callback.onStartProcess()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = generateImage(bitmap)
                val processId = response.body()?.process_id.orEmpty()

                if (processId.isEmpty()) {
                    reportError("Process ID not found", callback)
                    return@launch
                }

                delay(INITIAL_DELAY)
                pollImageProcessingStatus(processId, callback)

            } catch (e: Exception) {
                reportError(e, callback)
            }
        }
    }

    private suspend fun generateImage(bitmap: Bitmap): Response<MonsterApiService.MonsEffectResponse> {
        val tempFile = createTempFile(bitmap)
        val imagePart = MultipartBody.Part.createFormData(
            "init_image_url",
            tempFile.name,
            RequestBody.create(MEDIA_TYPE_IMAGE.toMediaTypeOrNull(), tempFile)
        )
        return apiService.generateImage("application/json", token, prompt, imagePart)
    }

    private suspend fun pollImageProcessingStatus(
        processId: String,
        callback: ImageEffect.ImageEffectCallback
    ) {
        repeat(MAX_RETRIES) { attempt ->
            RLog.d("ImageResponse", "Waiting for process to complete... Attempt $attempt")

            val imgResponse =
                apiService.getImageProcessingStatus(processId = processId, authorization = token)
            val status = imgResponse.body()?.status

            if (imgResponse.isSuccessful) {
                when (status) {
                    "COMPLETED" -> {
                        handleResponse(imgResponse, callback)
                        return
                    }

                    "IN_PROGRESS" -> {
                        RLog.d(
                            "ImageResponse",
                            "Processing in progress, retrying in $RETRY_DELAY ms"
                        )
                        delay(RETRY_DELAY)
                    }

                    else -> {
                        reportError("Unexpected status: $status", callback)
                        return
                    }
                }
            } else {
                reportError("Network error or unsuccessful response", callback)
                return
            }
        }
        reportError("Max retries reached without completion", callback)
    }

    private suspend fun handleResponse(
        response: Response<MonsterApiService.ImageProcessingResponse>,
        callback: ImageEffect.ImageEffectCallback
    ) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val effectResponse = response.body()
                if (effectResponse?.status == "COMPLETED") {
                    handleSuccess(effectResponse, callback)
                } else {
                    reportError("Image is still processing: ${effectResponse?.status}", callback)
                }
            } else {
                reportError("Failed to apply effect: ${response.message()}", callback)
            }
        }
    }

    private fun handleSuccess(
        effectResponse: MonsterApiService.ImageProcessingResponse,
        callback: ImageEffect.ImageEffectCallback
    ) {
        val imageUrl = effectResponse.result.output.firstOrNull() ?: return reportError(
            "No output image available", callback
        )
        RLog.e("FashionEffectResponse: finalImageLink", imageUrl)

        ImageLoader.getInstance()
            .loadBitmap(context, imageUrl, System.currentTimeMillis().toString(),
                object : OnImageLoadedListener2 {
                    override fun onImageLoaded(bitmap: Bitmap?, keyValue: String?, position: Int) {
                        callback.onSuccess(bitmap, keyValue)
                    }

                    override fun onErrorLoaded(url: String?, position: Int) {
                        reportError("Failed to load image", callback)
                    }
                })
    }

    private fun createTempFile(bitmap: Bitmap): File {
        val tempFile = File.createTempFile("upload_image", ".jpg", context.cacheDir)
        FileOutputStream(tempFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return tempFile
    }

    private fun reportError(message: String, callback: ImageEffect.ImageEffectCallback) {
        val exception = Exception(message)
        RLog.e("Error applying effect:", message)
        callback.onError(exception)
    }

    private fun reportError(exception: Exception, callback: ImageEffect.ImageEffectCallback) {
        RLog.e("Error applying effect:", exception.message)
        callback.onError(exception)
    }

    override fun applyEffectWithData(callback: ImageEffect.ImageEffectCallback, context: Context?) {
        // Implementation placeholder
    }

    override fun isBitmapHolder(): Boolean {
        return true
    }
}
