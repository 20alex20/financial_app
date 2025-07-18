package com.example.finances.core.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finances.features.transactions.data.database.TransactionsApi
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.example.finances.features.transactions.data.models.TransactionEntity
import com.example.finances.features.transactions.domain.DateTimeFormatters
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

    private val database by lazy {
        Room.databaseBuilder(applicationContext, FinanceDatabase::class.java, "finance_db").build()
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

    private suspend fun syncTransaction(transaction: TransactionEntity) {
        val transactionRequest = TransactionRequest(
            accountId = 49, // Using the same accountId as before
            categoryId = transaction.categoryId,
            amount = String.format(null, "%.2f", transaction.amount),
            transactionDate = transaction.dateTime.format(DateTimeFormatters.requestDateTime),
            comment = transaction.comment
        )

        try {
            val responseId = if (transaction.remoteId == null) {
                // Create new transaction on server
                val createResponse = transactionsApi.createTransaction(transactionRequest)
                Log.d(TAG, "Created transaction on server with ID: ${createResponse.id}")
                
                // Update local transaction with remote ID and synced status
                database.transactionDao().insertTransaction(
                    transaction.copy(
                        remoteId = createResponse.id,
                        isSynced = true
                    )
                )
                createResponse.id
            } else {
                // Update existing transaction on server
                val updateResponse = transactionsApi.updateTransaction(
                    transactionId = transaction.remoteId,
                    transaction = transactionRequest
                )
                Log.d(TAG, "Updated transaction on server with ID: ${transaction.remoteId}")
                
                // Update local transaction with synced status
                database.transactionDao().insertTransaction(
                    transaction.copy(isSynced = true)
                )
                updateResponse.id
            }
            
            Log.d(TAG, "Successfully synced transaction $responseId")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync transaction ${transaction.id}", e)
            throw e
        }
    }

    override suspend fun doWork(): Result {
        return try {
            val unsyncedTransactions = database.transactionDao().getNotSyncedTransactions()
            Log.d(TAG, "Found ${unsyncedTransactions.size} unsynced transactions")

            unsyncedTransactions.forEach { transaction ->
                try {
                    syncTransaction(transaction)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to sync transaction ${transaction.id}", e)
                    // Continue with next transaction even if this one failed
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Worker failed", e)
            Result.failure()
        }
    }
} 