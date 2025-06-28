package com.example.finances.features.history.domain.repo

import com.example.finances.common.models.Response
import com.example.finances.features.history.domain.models.HistoryRecord
import com.example.finances.common.models.Currency
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Интерфейс репозитория экрана истории
 */
interface HistoryRepo {
    fun getCurrency(): Flow<Response<Currency>>

    fun getHistory(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ): Flow<Response<List<HistoryRecord>>>
}
