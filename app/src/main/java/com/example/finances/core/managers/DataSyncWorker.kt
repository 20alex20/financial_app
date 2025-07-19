package com.example.finances.core.managers

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finances.core.utils.SyncTimeManager
import com.example.finances.core.utils.models.Currency
import com.example.finances.features.account.data.database.AccountApi
import com.example.finances.features.account.data.models.AccountUpdateRequest
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

class DataSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val database by lazy {
        Room.databaseBuilder(applicationContext, FinanceDatabase::class.java, "finance_db").build()
    }

    private val token = BufferedReader(
        InputStreamReader(
            applicationContext.resources.openRawResource(com.example.finances.R.raw.api_key)
        )
    ).readLine().trim()

    private val retrofit by lazy {
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
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val transactionsApi: TransactionsApi by lazy {
        retrofit.create(TransactionsApi::class.java)
    }

    private val accountApi: AccountApi by lazy {
        retrofit.create(AccountApi::class.java)
    }

    private suspend fun syncAccount() {
        val account = database.accountDao().getAccount()
        if (account != null && !account.isSynced) {
            try {
                val accountRequest = AccountUpdateRequest(
                    name = account.name,
                    balance = String.format(null, "%.2f", account.balance),
                    currency = Currency.valueOf(account.currency).shortName
                )
                accountApi.updateAccount(account.id, accountRequest)
                Log.d(TAG, "Successfully updated account ${account.id}")
                database.accountDao().insertAccount(account.copy(isSynced = true))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to sync account ${account.id}", e)
            }
        }
    }

    private suspend fun verifyTransactionOwnership(
        transactionId: Int,
        expectedAccountId: Int
    ): Boolean {
        return try {
            val remoteTransaction = transactionsApi.getTransaction(transactionId)
            remoteTransaction.account.id == expectedAccountId
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun syncTransaction(transaction: TransactionEntity, accountId: Int) {
        val transactionRequest = TransactionRequest(
            accountId = accountId,
            categoryId = transaction.categoryId,
            amount = String.format(null, "%.2f", transaction.amount),
            transactionDate = transaction.dateTime.format(DateTimeFormatters.requestDateTime),
            comment = transaction.comment
        )
        if (transaction.remoteId != null &&
            verifyTransactionOwnership(transaction.remoteId, accountId)
        ) {
            transactionsApi.updateTransaction(transaction.remoteId, transactionRequest)
            Log.d(TAG, "Updated transaction on server with ID: ${transaction.remoteId}")
            database.transactionDao().insertTransaction(transaction.copy(isSynced = true))
        } else {
            val responseId = transactionsApi.createTransaction(transactionRequest).id
            Log.d(TAG, "Created transaction on server with ID: $responseId")
            database.transactionDao().insertTransaction(
                transaction.copy(remoteId = responseId, isSynced = true)
            )
        }
    }

    override suspend fun doWork(): Result {
        return try {
            syncAccount()
            val unsyncedTransactions = database.transactionDao().getNotSyncedTransactions()
            Log.d(TAG, "Found ${unsyncedTransactions.size} unsynced transactions")
            database.accountDao().getAccount()?.also { account ->
                var synced = false
                unsyncedTransactions.forEach { transaction ->
                    try {
                        syncTransaction(transaction, account.id)
                        synced = true
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to sync transaction with local ID: ${transaction.id}", e)
                    }
                }
                if (synced) SyncTimeManager.updateLastSyncTime(applicationContext)
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Worker failed", e)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "DataSyncWorker"
        private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    }
}
