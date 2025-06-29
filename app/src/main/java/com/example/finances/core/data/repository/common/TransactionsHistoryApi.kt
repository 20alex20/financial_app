package com.example.finances.core.data.repository.common

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Источник данных (API для загрузки)
 */
interface TransactionsHistoryApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>
}
