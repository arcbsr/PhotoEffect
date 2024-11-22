package com.crop.networklibs.apis

import com.crop.modellbs.avatar.AvatarEffectRequest
import com.crop.modellbs.fashion.FashionEffectRequest
import com.crop.modellbs.imgtoimg.Image2ImageRequest
import com.crop.modellbs.removebg.ImageFetchRequest
import com.crop.modellbs.removebg.ImageRemoveBGRequest
import com.crop.modellbs.uploadimg.ImageUploadRequest
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class EffectResponse(
    val status: String,
    val fetch_result: String,
    @field:SerializedName("future_links") val outputImageLinks: List<String>, // Add this field
    @field:SerializedName("output") val output: List<String>, // Add this field
    val message: String,
    val id: Int
)

data class UploadResponse(
    @field:SerializedName("link") val link: String,
    val messege: String
)

interface ModelsLabApiService {
    @POST("v6/image_editing/fashion")
    suspend fun applyFashionEffect(@Body request: FashionEffectRequest): Response<EffectResponse>

    @POST("v6/image_editing/avatar_gen")
    suspend fun applyAvatarEffect(@Body request: AvatarEffectRequest): Response<EffectResponse>

    @POST("v6/realtime/img2img")
    suspend fun applyimg2imgEffect(@Body request: Image2ImageRequest): Response<EffectResponse>

    @POST("v6/realtime/text2img")
    suspend fun applytxtimgEffect(@Body request: Image2ImageRequest): Response<EffectResponse>

    @POST("v3/base64_crop")
    suspend fun applyImgUpload(@Body request: ImageUploadRequest): Response<UploadResponse>

    @POST("v6/image_editing/removebg_mask")
    suspend fun applyRemoveBG(@Body request: ImageRemoveBGRequest): Response<EffectResponse>

    @POST("v6/image_editing/fetch/{processId}")
    suspend fun getImageProcessingStatus(
        @Path("processId") processId: String,
        @Header("accept") accept: String = "application/json",
        @Body request: ImageFetchRequest
    ): Response<EffectResponse>
}