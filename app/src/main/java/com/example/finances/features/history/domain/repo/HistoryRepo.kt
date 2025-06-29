package com.example.finances.features.history.domain.repo

import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.history.domain.models.HistoryRecord
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
