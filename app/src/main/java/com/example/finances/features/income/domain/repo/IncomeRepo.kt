package com.example.finances.features.income.domain.repo

import com.example.finances.common.models.Response
import com.example.finances.common.models.Currency
import com.example.finances.features.income.domain.models.Income
import kotlinx.coroutines.flow.Flow

interface IncomeRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getIncome(): Flow<Response<List<Income>>>
}
