package com.crop.phototocartooneffect.renderengins.apis.fashion

import android.content.Context
import android.graphics.Bitmap
import com.crop.modellbs.fashion.FashionEffectRequest
import com.crop.networklibs.apis.ApiClient
import com.crop.networklibs.apis.ModelsLabApiService
import com.crop.phototocartooneffect.renderengins.ImageEffect
import com.crop.phototocartooneffect.renderengins.ImageEffect.ImageEffectCallback
import com.crop.phototocartooneffect.utils.RLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FashionEffect(
    effectRequest: FashionEffectRequest

) : ImageEffect {

    private val fashionEffectRequest: FashionEffectRequest = effectRequest
    private val apiService: ModelsLabApiService = ApiClient.modelsLabApiService

    init {
//        val retrofit = Retrofit.Builder().baseUrl("https://modelslab.com/api/v6/")
//            .addConverterFactory(GsonConverterFactory.create()).build()

    }

    override fun applyEffectWithData(callback: ImageEffectCallback, context: Context) {

        // Log fashionEffectRequest as JSON string
//        val gson = com.google.gson.Gson()
//        val jsonString = gson.toJson(fashionEffectRequest)
        RLog.d("FashionEffectRequest:", fashionEffectRequest)


        // Launch the network request in a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.applyFashionEffect(
                    fashionEffectRequest
                )
                // Check if the response is successful
                if (response.isSuccessful && response.body() != null) {
                    val fashionEffectResponse = response.body()!!

                    // If status is "processing", wait based on ETA
//                    if (fashionEffectResponse.status == "processing") {
//                        withContext(Dispatchers.Main) {
//                            Log.d(
//                                "FashionEffect",
//                                "Waiting for processing to complete..." + fashionEffectResponse.status
//                            )
////                            callback.onSuccess(fashionEffectResponse.fetch_result)
//                        }
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            callback.onError(Exception("Image processing failed: ${fashionEffectResponse.message}"))
//                            Log.e(
//                                "FashionEffect",
//                                "Failed to apply effect: ${fashionEffectResponse.message}"
//                            )
//                        }
//                    }
                    if (fashionEffectResponse.outputImageLinks.isNotEmpty()) {
                        withContext(Dispatchers.Main) {

                            RLog.e(
                                "FashionEffectResponse:", fashionEffectResponse.outputImageLinks
                            )
//                            ImageLoader.getInstance().loadBitmap(
//                                context,
//                                "https://pub-3626123a908346a7a8be8d9295f44e26.r2.dev/temp/be4ccd86-d200-4588-a256-de00da62fa4b.png",
//                                "newimage.png"
//                            ) { bitmap: Bitmap?, _: Exception? ->
//                                if (bitmap != null) {
//                                    callback.onSuccess(bitmap)
//                                } else {
//                                    callback.onError(Exception("Failed to load bitmap"))
//                                }
//                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            callback.onError(Exception("Image processing failed: ${fashionEffectResponse.message}"))
                            RLog.e(
                                "Failed to apply effect: ", fashionEffectResponse.message
                            )
                        }
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        RLog.e("Failed to apply effect222: ", response.message())
                        callback.onError(Exception("Failed to apply effect: ${response.message()}"))

                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    RLog.e("Error applying effect: ", e.message)
                    callback.onError(e)
                }
            }
        }
    }

    override fun applyEffect(bitmap: Bitmap, callback: ImageEffectCallback) {
        TODO("Not yet implemented")
    }

}
