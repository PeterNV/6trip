package com.example.a6trip.data.auth

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CloudinaryClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.cloudinary.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: CloudinaryApi = retrofit.create(CloudinaryApi::class.java)
}