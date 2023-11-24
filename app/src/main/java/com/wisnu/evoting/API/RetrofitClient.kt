package com.wisnu.evoting.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.4.204.86/e-voting---php-native/api/"
//    private const val BASE_URL = "http://192.168.1.10/votesystem/api/"

    val instance: APIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(APIService::class.java)
    }
}