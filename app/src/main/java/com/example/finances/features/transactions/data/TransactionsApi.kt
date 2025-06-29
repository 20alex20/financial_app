package com.example.finances.features.transactions.data

import com.example.finances.features.transactions.data.models.TransactionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Источник данных (API для загрузки)
 */
interface TransactionsApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>
}
