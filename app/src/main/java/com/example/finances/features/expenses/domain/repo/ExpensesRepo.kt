package com.example.finances.features.expenses.domain.repo

import com.example.finances.common.models.Response
import com.example.finances.common.models.Currency
import com.example.finances.features.expenses.domain.models.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория экрана расходов
 */
interface ExpensesRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getExpenses(): Flow<Response<List<Expense>>>
}
