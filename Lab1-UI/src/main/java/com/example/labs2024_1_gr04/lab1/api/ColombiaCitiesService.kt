package com.example.labs2024_1_gr04.lab1.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ColombiaCitiesService {
    @GET("City/search/{name}")
    suspend fun fetchCities(@Path("name") name: String): Response<List<City>>
}

object RetrofitInstance {
    val apiService: ColombiaCitiesService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-colombia.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ColombiaCitiesService::class.java)
    }
}