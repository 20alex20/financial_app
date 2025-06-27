package com.example.finances.features.network.domain

import android.content.Context
import com.example.finances.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

object NetworkManager {
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    @Volatile
    private var apiKey: String? = null

    fun <T> provideApi(context: Context, apiClass: Class<T>): T {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(createAuthInterceptor(apiKey ?: readApiKey(context)))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(apiClass)
    }

    private fun createAuthInterceptor(apiKey: String) = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        chain.proceed(request)
    }

    private fun readApiKey(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.api_key)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.readLine().trim().also { apiKey = it }
    }
}
