package com.crop.networklibs.apis

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MonsApiClient {
    private const val BASE_URL = "https://api.monsterapi.ai/v1/"

    private val client = OkHttpClient.Builder().build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val monsLabApiService: MonsterApiService by lazy {
        retrofit.create(MonsterApiService::class.java)
    }
}
