package com.example.financial_app.features.income.domain.repo

import com.example.financial_app.common.models.Response
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import kotlinx.coroutines.flow.Flow

interface IncomeRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getIncome(): Flow<Response<List<Income>>>
}
