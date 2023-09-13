package com.couponusers.couponuser.Utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {

    val baseUrl = "https://coupon.rktaxsolution.com/api/"
    val imageUrl = "https://coupon.corewave.us/uploads/"



    fun getInstance(): Retrofit{

        val logging =HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client =OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}