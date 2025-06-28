package com.example.finances.features.history.data

import com.example.finances.features.history.data.models.TransactionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Источник данных экрана истории (API для загрузки истории)
 */
interface HistoryApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>
}
