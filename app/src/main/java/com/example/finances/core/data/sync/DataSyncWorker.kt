package com.example.finances.core.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finances.core.data.local.FinanceDatabase
import com.example.finances.core.utils.NetworkConnectionObserver
import com.example.finances.features.account.data.AccountApi
import com.example.finances.features.categories.data.CategoriesApi
import com.example.finances.features.transactions.data.TransactionsApi
import com.example.finances.core.data.local.entities.AccountEntity
import com.example.finances.core.data.local.entities.CategoryEntity
import com.example.finances.core.data.local.entities.TransactionEntity
import com.example.finances.features.account.data.mappers.toAccount
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.transactions.data.mappers.toTransaction
import kotlinx.coroutines.flow.first
import retrofit2.Retrofit

class DataSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val retrofit: Retrofit
) : CoroutineWorker(context, params) {

    private val database = FinanceDatabase.getInstance(context)
    private val networkObserver = NetworkConnectionObserver(context)
    private val accountApi = retrofit.create(AccountApi::class.java)
    private val categoriesApi = retrofit.create(CategoriesApi::class.java)
    private val transactionsApi = retrofit.create(TransactionsApi::class.java)

    override suspend fun doWork(): Result {
        // Check for internet connectivity
        if (!networkObserver.observe().first()) {
            return Result.retry()
        }

        try {
            // Sync account data
            val remoteAccount = accountApi.getAccounts().first().toAccount()
            database.accountDao().insertAccount(AccountEntity.fromAccount(remoteAccount))

            // Sync categories
            val remoteCategories = categoriesApi.getCategories().map { it.toCategory() }
            database.categoryDao().insertCategories(remoteCategories.map { CategoryEntity.fromCategory(it) })

            // Sync unsynced transactions to server
            val unsyncedTransactions = database.transactionDao().getUnsyncedTransactions()
            for (transaction in unsyncedTransactions) {
                try {
                    val request = com.example.finances.features.transactions.data.models.TransactionRequest(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = String.format(null, "%.2f", transaction.amount),
                        transactionDate = transaction.transactionDate,
                        comment = transaction.comment
                    )

                    if (transaction.id == null) {
                        transactionsApi.createTransaction(request)
                    } else {
                        transactionsApi.updateTransaction(transaction.id, request)
                    }
                    database.transactionDao().markTransactionSynced(transaction.id ?: continue)
                } catch (e: Exception) {
                    // Log error but continue with other transactions
                    e.printStackTrace()
                }
            }

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }
} 