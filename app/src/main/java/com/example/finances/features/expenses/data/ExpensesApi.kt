package com.example.finances.features.expenses.data

import com.example.finances.features.expenses.data.models.TransactionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Источник данных экрана расходов (API для загрузки расходов)
 */
interface ExpensesApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>
}
