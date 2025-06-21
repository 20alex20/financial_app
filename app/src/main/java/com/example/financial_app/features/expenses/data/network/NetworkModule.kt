package com.example.financial_app.features.expenses.data.network

import android.content.Context
import com.example.financial_app.R
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

object NetworkModule {
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    fun provideFinanceApi(context: Context): FinanceApi {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val apiKey = readApiKey(context)
        val client = OkHttpClient.Builder()
            .addInterceptor(createAuthInterceptor(apiKey))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(FinanceApi::class.java)
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
        return reader.readLine().trim()
    }
}
