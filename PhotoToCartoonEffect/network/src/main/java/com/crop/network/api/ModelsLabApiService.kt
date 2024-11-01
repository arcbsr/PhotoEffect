package com.crop.network.api

import com.crop.modellbs.fashion.FashionEffectRequest
import com.crop.modellbs.imgtoimg.TextToImageRequest
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class FashionEffectResponse(
    val status: String,
    val fetch_result: String,
    @field:SerializedName("future_links") val outputImageLinks: List<String>, // Add this field
    @field:SerializedName("output") val output: List<String>, // Add this field
    val message: String
)

interface ModelsLabApiService {
    @POST("v6/image_editing/fashion")
    suspend fun applyFashionEffect(@Body request: FashionEffectRequest): Response<FashionEffectResponse>

    @POST("v3/img2img")
    suspend fun applyimg2imgEffect(@Body request: TextToImageRequest): Response<FashionEffectResponse>
}