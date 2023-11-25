package com.reyaz.nearbyphone.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    fun getService(): ApiService {
        val defaultRetrofitClient = createRetrofitClient("https://api.seatgeek.com/")
        return defaultRetrofitClient.create(ApiService::class.java)
    }

    private fun createRetrofitClient(url: String): Retrofit {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        with(okHttpClientBuilder) {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
            addInterceptor(networkInterceptor())
        }

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
    }

    private fun networkInterceptor() = Interceptor { chain ->
        val url = chain.request().url.newBuilder().apply {
            addQueryParameter("client_id","Mzg0OTc0Njl8MTcwMDgxMTg5NC44MDk2NjY5")
        }.build()
        val request = chain.request().newBuilder().url(url).build()
        return@Interceptor chain.proceed(request)
    }
}
