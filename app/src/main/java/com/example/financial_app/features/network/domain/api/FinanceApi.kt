package com.example.financial_app.features.network.domain.api

import com.example.financial_app.features.network.domain.models.AccountResponse
import com.example.financial_app.features.network.domain.models.TransactionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FinanceApi {
    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>
}
