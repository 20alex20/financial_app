package com.example.finances.features.transactions.domain.repository

import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.transactions.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория экрана расходов
 */
interface TransactionsRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getTransactions(isIncome: Boolean): Flow<Response<List<Transaction>>>
}
