package com.example.finances.features.transactions.domain.repository

import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import java.time.LocalDate

/**
 * Интерфейс репозитория транзакций
 */
interface TransactionsRepo {
    suspend fun getCurrency(): Response<Currency>

    suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ): Response<List<Transaction>>
}
