package com.crop.phototocartooneffect.renderengins.apis.ailabs

import android.content.Context
import android.graphics.Bitmap
import com.crop.networklibs.apis.AILabApiService
import com.crop.networklibs.apis.AILabsApiClient
import com.crop.networklibs.apis.MonsApiClient
import com.crop.networklibs.apis.MonsterApiService
import com.crop.phototocartooneffect.enums.EditingCategories
import com.crop.phototocartooneffect.imageloader.ImageLoader
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

class AiLabApiClient(
    tokenText: String,
    private val context: Context,
    promptText: String,
    imageCreationType: EditingCategories.ImageCreationType
) : ImageEffect {

    private val apiService: AILabApiService = AILabsApiClient.aiLabApiService
    private val token = "$tokenText"
    private val prompt = RequestBody.create("text/plain".toMediaTypeOrNull(), promptText)
    private val imageCreationType = imageCreationType
    private lateinit var job: Job

    companion object {
        const val MAX_RETRIES = 5
        const val RETRY_DELAY = 5000L
        const val INITIAL_DELAY = 2000L
        private const val MEDIA_TYPE_IMAGE = "image/jpeg"
    }

    override fun applyEffect(bitmap: Bitmap, callback: ImageEffect.ImageEffectCallback) {

        callback.onStartProcess()
        job = CoroutineScope(Dispatchers.IO).launch {
//            try {
            RLog.e("FashionEffectResponse", "applyEffect: ")
            val response = generateImage(bitmap)
            handleResponse(response, callback)

//            } catch (e: Exception) {
//                reportError(e, callback)
//                RLog.e("FashionEffectResponse", "Exception: ${e.message}")
//            }
        }
    }

    //TODO: Cancel job
    public fun cancel() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }

    private suspend fun generateImage(bitmap: Bitmap): Response<AILabApiService.ApiResponse> {
        RLog.e("imageCreationType", " generateImage")
        val tempFile = createTempFile(bitmap)
        RLog.e("imageCreationType", " generateImage done")
        val imagePart = MultipartBody.Part.createFormData(
            "image_target",
            tempFile.name,
            RequestBody.create(MEDIA_TYPE_IMAGE.toMediaTypeOrNull(), tempFile)
        )
        RLog.e("imageCreationType:", " ${imageCreationType.name}")
//        return when (imageCreationType) {
//            EditingCategories.ImageCreationType.MONSTER_AI_IMG_TO_IMG -> {
//                apiService.generateImage("application/json", token, imagePart, 16)
//            }
//
//            EditingCategories.ImageCreationType.MONSTER_AI_PIX_TO_PIX -> {
//                apiService.generateImagepix2pix("application/json", token, prompt, imagePart)
//            }
//
//            EditingCategories.ImageCreationType.MONSTER_AI_PHOTO_MAKER -> {
//                apiService.generateImagePhotoMaker("application/json", token, prompt, imagePart)
//            }
//
//            else -> {
//                apiService.generateImage("application/json", token, prompt, imagePart)
//            }
//        }
        return apiService.generateImage(token, imagePart, RequestBody.create("text/plain".toMediaTypeOrNull(), "16"))
    }

    private suspend fun handleResponse(
        response: Response<AILabApiService.ApiResponse>,
        callback: ImageEffect.ImageEffectCallback
    ) {
        withContext(Dispatchers.Main) {
            RLog.e("rafiur>>>", "handleResponse: " + response.body().toString());
            if (response.isSuccessful) {
                val effectResponse = response.body()
                if (effectResponse?.data?.image.isNullOrEmpty()) {

                    reportError(
                        "Image Creation error", callback
                    )
                } else {
                    if (effectResponse != null) {
                        handleSuccess(effectResponse, callback)
                    } else {
                        reportError("Failed to apply effect: ${response.message()}", callback)
                    }
                }
            } else {
                reportError("Failed to apply effect: ${response.message()}", callback)
            }
        }
    }

    private fun handleSuccess(
        effectResponse: AILabApiService.ApiResponse,
        callback: ImageEffect.ImageEffectCallback
    ) {

        val imageUrl = effectResponse.data.image.firstOrNull() ?: return reportError(
            "No output image available", callback
        )
        RLog.e("FashionEffectResponse: finalImageLink", imageUrl)

        val bitmap: Bitmap = ImageLoader.getInstance().base64ToBitmap(effectResponse.data.image)
        if (bitmap == null) {
            reportError("Failed to load image", callback)
            return
        }
        val keyValue: String = System.currentTimeMillis().toString()
        ImageLoader.getInstance().loadBitmap(keyValue, bitmap);
        callback.onSuccess(bitmap, keyValue)
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

    override fun applyEffectWithData(
        callback: ImageEffect.ImageEffectCallback, context: Context?
    ) {
        // Implementation placeholder
    }

    override fun isBitmapHolder(): Boolean {
        return true
    }
}
