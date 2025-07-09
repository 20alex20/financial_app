package com.example.finances.core.di.modules

import android.content.Context
import com.example.finances.R
import com.example.finances.core.di.ApiKey
import com.example.finances.core.di.ApplicationContext
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Singleton

/**
 * Генерирует запрос к серверу по заданному API
 */
@Module
class RetrofitClientModule {
    @Provides
    fun providesRetrofit(@ApiKey token: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
                )
            })
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    @ApiKey
    fun providesApiKey(@ApplicationContext context: Context) = BufferedReader(
        InputStreamReader(context.resources.openRawResource(R.raw.api_key))
    ).readLine().trim()

    companion object {
        private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    }
}
