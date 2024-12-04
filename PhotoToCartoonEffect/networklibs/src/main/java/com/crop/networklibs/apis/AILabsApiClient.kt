package com.crop.networklibs.apis

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AILabsApiClient {
    private const val BASE_URL = "https://www.ailabapi.com"

    private val client = OkHttpClient.Builder().build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val aiLabApiService: AILabApiService by lazy {
        retrofit.create(AILabApiService::class.java)
    }
}
