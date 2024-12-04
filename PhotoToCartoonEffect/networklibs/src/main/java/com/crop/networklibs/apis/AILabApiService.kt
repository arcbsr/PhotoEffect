package com.crop.networklibs.apis

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

public interface AILabApiService {
    data class ApiResponse(
        val data: Data,
        val error_code: Int,
        val error_detail: ErrorDetail,
        val log_id: String,
        val request_id: String,
        val time_elapsed: String
    )

    data class Data(
        val image: String
    )

    data class ErrorDetail(
        val status_code: Int,
        val code: String,
        val code_message: String,
        val message: String
    )

    @Multipart
    @POST("/api/portrait/effects/emotion-editor")
    suspend fun generateImage(
        @Header("ailabapi-api-key") token: String,
        @Part image_target: MultipartBody.Part,
        @Part ("service_choice") target: RequestBody,
// @Part("prompt") prompt: RequestBody,
    ): Response<ApiResponse>

}
