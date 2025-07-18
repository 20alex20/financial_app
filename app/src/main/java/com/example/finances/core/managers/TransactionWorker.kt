package com.example.finances.core.managers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finances.features.transactions.data.database.TransactionsApi
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class TransactionWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "TransactionWorker"
        private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    }

    private val transactionsApi: TransactionsApi by lazy {
        val token = BufferedReader(
            InputStreamReader(applicationContext.resources.openRawResource(com.example.finances.R.raw.api_key))
        ).readLine().trim()

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

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(TransactionsApi::class.java)
    }

    override suspend fun doWork(): Result {
        try {
            val transaction = TransactionRequest(
                accountId = 49,
                categoryId = 1,
                amount = "500.00",
                transactionDate = "2025-07-18T22:59:46.599Z",
                comment = "Yeeeeesssssss"
            )
            
            transactionsApi.createTransaction(transaction)
            Log.d(TAG, "Transaction sent successfully!")
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send transaction", e)
            return Result.failure()
        }
    }
} 