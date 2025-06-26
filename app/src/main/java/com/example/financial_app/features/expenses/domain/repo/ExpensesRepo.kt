package com.example.financial_app.features.expenses.domain.repo

import com.example.financial_app.common.models.Response
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.expenses.domain.models.Expense
import kotlinx.coroutines.flow.Flow

interface ExpensesRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getExpenses(): Flow<Response<List<Expense>>>
}
