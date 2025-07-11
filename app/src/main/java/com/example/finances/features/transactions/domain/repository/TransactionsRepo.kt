package com.example.finances.features.transactions.domain.repository

import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.models.Currency
import com.example.finances.features.transactions.domain.models.ShortCategory
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.models.Transaction
import java.time.LocalDate

/**
 * Интерфейс репозитория транзакций
 */
interface TransactionsRepo {
    suspend fun getCurrency(): Response<Currency>

    suspend fun getCategories(isIncome: Boolean): Response<List<ShortCategory>>

    suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ): Response<List<Transaction>>

    suspend fun getTransaction(transactionId: Int): Response<ShortTransaction>

    suspend fun createTransaction(shortTransaction: ShortTransaction): Response<ShortTransaction>

    suspend fun updateTransaction(shortTransaction: ShortTransaction): Response<ShortTransaction>
}
