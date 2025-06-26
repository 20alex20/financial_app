package com.example.financial_app.features.history.domain.repo

import com.example.financial_app.common.models.Response
import com.example.financial_app.features.history.domain.models.HistoryRecord
import com.example.financial_app.common.models.Currency
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HistoryRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getHistory(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ): Flow<Response<List<HistoryRecord>>>
}
