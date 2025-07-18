package com.example.finances.core.managers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finances.features.account.data.database.AccountApi
import com.example.finances.features.account.data.mappers.toAccount
import com.example.finances.features.account.data.mappers.toAccountEntity
import com.example.finances.features.account.data.mappers.toAccountUpdateRequest
import com.example.finances.features.account.data.mappers.toShortAccount
import com.example.finances.features.transactions.data.database.TransactionsApi
import com.example.finances.features.transactions.data.mappers.toShortTransaction
import com.example.finances.features.transactions.data.mappers.toTransactionEntity
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.example.finances.features.transactions.domain.DateTimeFormatters
import retrofit2.Retrofit

class DataSyncWorker(
    context: Context,
    params: WorkerParameters,
    retrofit: Retrofit,
    private val database: FinanceDatabase
) : CoroutineWorker(context, params) {
    private val accountApi = retrofit.create(AccountApi::class.java)
    private val transactionsApi = retrofit.create(TransactionsApi::class.java)

    override suspend fun doWork(): Result {
        try {
            syncAccount()
            syncTransactions()
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    private suspend fun syncAccount() {
        val account = database.accountDao().getAccount()
        if (account != null && !account.isSynced) {
            val updatedAccount = accountApi.updateAccount(
                accountId = account.id,
                account = account.toShortAccount().toAccountUpdateRequest()
            ).toAccount().toAccountEntity()
            database.accountDao().updateAccount(updatedAccount)
        }
    }

    private suspend fun syncTransactions() {
        val unsyncedTransactions = database.transactionDao().getNotSyncedTransactions()
        for (transaction in unsyncedTransactions) {
            try {
                val account = database.accountDao().getAccount() ?: continue
                val request = TransactionRequest(
                    accountId = account.id,
                    categoryId = transaction.categoryId,
                    amount = String.format(null, "%.2f", transaction.amount),
                    transactionDate = transaction.dateTime.format(
                        DateTimeFormatters.requestDateTime
                    ),
                    comment = transaction.comment
                )
                var remoteId: Int
                val response = if (transaction.remoteId != null) {
                    transactionsApi.updateTransaction(
                        transactionId = transaction.remoteId,
                        transaction = request
                    ).also { remoteId = it.id }.toShortTransaction()
                } else transactionsApi.createTransaction(request).also {
                    remoteId = it.id
                }.toShortTransaction(transaction.categoryName, transaction.categoryEmoji)
                database.transactionDao().insertTransaction(
                    response.toTransactionEntity(
                        id = transaction.id,
                        remoteId = remoteId,
                        isIncome = transaction.isIncome,
                        isSynced = true
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
